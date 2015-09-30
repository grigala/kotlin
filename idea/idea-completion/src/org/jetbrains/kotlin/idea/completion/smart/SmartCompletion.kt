/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.idea.completion.smart

import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.completion.OffsetKey
import com.intellij.codeInsight.completion.PrefixMatcher
import com.intellij.codeInsight.lookup.Lookup
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementDecorator
import com.intellij.codeInsight.lookup.LookupElementPresentation
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.SmartList
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.idea.caches.resolve.resolveToDescriptor
import org.jetbrains.kotlin.idea.completion.*
import org.jetbrains.kotlin.idea.resolve.ResolutionFacade
import org.jetbrains.kotlin.idea.util.CallTypeAndReceiver
import org.jetbrains.kotlin.idea.util.FuzzyType
import org.jetbrains.kotlin.idea.util.isAlmostEverything
import org.jetbrains.kotlin.lexer.JetTokens
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.DescriptorUtils
import org.jetbrains.kotlin.types.JetType
import org.jetbrains.kotlin.types.TypeUtils
import org.jetbrains.kotlin.types.typeUtil.makeNotNullable
import org.jetbrains.kotlin.utils.addToStdlib.check
import org.jetbrains.kotlin.utils.addToStdlib.singletonList
import org.jetbrains.kotlin.utils.addToStdlib.singletonOrEmptySet
import java.util.*

interface InheritanceItemsSearcher {
    fun search(nameFilter: (String) -> Boolean, consumer: (LookupElement) -> Unit)
}

class SmartCompletion(
        private val expression: JetExpression,
        private val resolutionFacade: ResolutionFacade,
        private val bindingContext: BindingContext,
        private val visibilityFilter: (DeclarationDescriptor) -> Boolean,
        private val prefixMatcher: PrefixMatcher,
        private val inheritorSearchScope: GlobalSearchScope,
        private val toFromOriginalFileMapper: ToFromOriginalFileMapper,
        private val lookupElementFactory: LookupElementFactory,
        private val callTypeAndReceiver: CallTypeAndReceiver<*, *>,
        private val forBasicCompletion: Boolean = false
) {
    private val expressionWithType = when (callTypeAndReceiver) {
        is CallTypeAndReceiver.DEFAULT ->
            expression

        is CallTypeAndReceiver.DOT,
        is CallTypeAndReceiver.SAFE,
        is CallTypeAndReceiver.INFIX,
        is CallTypeAndReceiver.CALLABLE_REFERENCE ->
            expression.parent as JetExpression

        else -> // actually no smart completion for such places
            expression
    }

    public val expectedInfos: Collection<ExpectedInfo> = calcExpectedInfos(expressionWithType)

    public val smartCastCalculator: SmartCastCalculator by lazy(LazyThreadSafetyMode.NONE) {
        SmartCastCalculator(bindingContext, resolutionFacade.moduleDescriptor, expression)
    }

    public val descriptorFilter: ((DeclarationDescriptor) -> Collection<LookupElement>)?
            = { descriptor: DeclarationDescriptor -> filterDescriptor(descriptor).map { postProcess(it) } }.check { expectedInfos.isNotEmpty() }

    public fun additionalItems(): Pair<Collection<LookupElement>, InheritanceItemsSearcher?> {
        val (items, inheritanceSearcher) = additionalItemsNoPostProcess()
        val postProcessedItems = items.map { postProcess(it) }
        //TODO: could not use "let" because of KT-8754
        val postProcessedSearcher = if (inheritanceSearcher != null)
            object : InheritanceItemsSearcher {
                override fun search(nameFilter: (String) -> Boolean, consumer: (LookupElement) -> Unit) {
                    inheritanceSearcher.search(nameFilter, { consumer(postProcess(it)) })
                }
            }
        else
            null
        return postProcessedItems to postProcessedSearcher
    }

    public val descriptorsToSkip: Set<DeclarationDescriptor> by lazy<Set<DeclarationDescriptor>>(LazyThreadSafetyMode.NONE) {
        val parent = expressionWithType.getParent()
        when (parent) {
            is JetBinaryExpression -> {
                if (parent.getRight() == expressionWithType) {
                    val operationToken = parent.getOperationToken()
                    if (operationToken == JetTokens.EQ || operationToken in COMPARISON_TOKENS) {
                        val left = parent.getLeft()
                        if (left is JetReferenceExpression) {
                            return@lazy bindingContext[BindingContext.REFERENCE_TARGET, left].singletonOrEmptySet()
                        }
                    }
                }
            }

            is JetWhenConditionWithExpression -> {
                val entry = parent.getParent() as JetWhenEntry
                val whenExpression = entry.getParent() as JetWhenExpression
                val subject = whenExpression.getSubjectExpression() ?: return@lazy emptySet()

                val descriptorsToSkip = HashSet<DeclarationDescriptor>()

                if (subject is JetSimpleNameExpression) {
                    val variable = bindingContext[BindingContext.REFERENCE_TARGET, subject] as? VariableDescriptor
                    if (variable != null) {
                        descriptorsToSkip.add(variable)
                    }
                }

                val subjectType = bindingContext.getType(subject) ?: return@lazy emptySet()
                val classDescriptor = TypeUtils.getClassDescriptor(subjectType)
                if (classDescriptor != null && DescriptorUtils.isEnumClass(classDescriptor)) {
                    val conditions = whenExpression.getEntries()
                            .flatMap { it.getConditions().toList() }
                            .filterIsInstance<JetWhenConditionWithExpression>()
                    for (condition in conditions) {
                        val selectorExpr = (condition.getExpression() as? JetDotQualifiedExpression)
                                                   ?.getSelectorExpression() as? JetReferenceExpression ?: continue
                        val target = bindingContext[BindingContext.REFERENCE_TARGET, selectorExpr] as? ClassDescriptor ?: continue
                        if (DescriptorUtils.isEnumEntry(target)) {
                            descriptorsToSkip.add(target)
                        }
                    }
                }

                return@lazy descriptorsToSkip
            }
        }
        return@lazy emptySet()
    }

    private fun filterDescriptor(descriptor: DeclarationDescriptor): Collection<LookupElement> {
        if (descriptor in descriptorsToSkip) return emptyList()

        val result = SmartList<LookupElement>()
        val types = descriptor.fuzzyTypesForSmartCompletion(smartCastCalculator, callTypeAndReceiver.callType, resolutionFacade)
        val infoClassifier = { expectedInfo: ExpectedInfo -> types.classifyExpectedInfo(expectedInfo) }

        result.addLookupElements(descriptor, expectedInfos, infoClassifier, noNameSimilarityForReturnItself = callTypeAndReceiver is CallTypeAndReceiver.DEFAULT) { descriptor ->
            lookupElementFactory.createLookupElementsInSmartCompletion(descriptor, bindingContext, true)
        }

        if (callTypeAndReceiver is CallTypeAndReceiver.DEFAULT) {
            toFunctionReferenceLookupElement(descriptor, expectedInfos.filterFunctionExpected())?.let { result.add(it) }
        }

        return result
    }

    private fun additionalItemsNoPostProcess(): Pair<Collection<LookupElement>, InheritanceItemsSearcher?> {
        val asTypePositionItems = buildForAsTypePosition()
        if (asTypePositionItems != null) {
            assert(expectedInfos.isEmpty())
            return Pair(asTypePositionItems, null)
        }

        val items = ArrayList<LookupElement>()
        val inheritanceSearchers = ArrayList<InheritanceItemsSearcher>()

        if (expectedInfos.isNotEmpty() && callTypeAndReceiver is CallTypeAndReceiver.DEFAULT) {
            TypeInstantiationItems(resolutionFacade, bindingContext, visibilityFilter, toFromOriginalFileMapper, inheritorSearchScope, lookupElementFactory, forBasicCompletion)
                    .addTo(items, inheritanceSearchers, expectedInfos)

            if (expression is JetSimpleNameExpression) {
                StaticMembers(bindingContext, lookupElementFactory).addToCollection(items, expectedInfos, expression, descriptorsToSkip)
            }

            if (!forBasicCompletion) {
                items.addThisItems(expression, expectedInfos, smartCastCalculator)

                LambdaItems.addToCollection(items, expectedInfos)

                KeywordValues.addToCollection(items, expectedInfos, expression)
            }

            MultipleArgumentsItemProvider(bindingContext, smartCastCalculator).addToCollection(items, expectedInfos, expression)
        }

        val inheritanceSearcher = if (inheritanceSearchers.isNotEmpty())
            object : InheritanceItemsSearcher {
                override fun search(nameFilter: (String) -> Boolean, consumer: (LookupElement) -> Unit) {
                    inheritanceSearchers.forEach { it.search(nameFilter, consumer) }
                }
            }
        else
            null

        return Pair(items, inheritanceSearcher)
    }

    private fun postProcess(item: LookupElement): LookupElement {
        if (forBasicCompletion) return item

        return if (item.getUserData(KEEP_OLD_ARGUMENT_LIST_ON_TAB_KEY) == null) {
            object : LookupElementDecorator<LookupElement>(item) {
                override fun handleInsert(context: InsertionContext) {
                    if (context.getCompletionChar() == Lookup.REPLACE_SELECT_CHAR) {
                        val offset = context.getOffsetMap().getOffset(OLD_ARGUMENTS_REPLACEMENT_OFFSET)
                        if (offset != -1) {
                            context.getDocument().deleteString(context.getTailOffset(), offset)
                        }
                    }

                    super.handleInsert(context)
                }
            }
        }
        else {
            item
        }
    }

    private fun MutableCollection<LookupElement>.addThisItems(place: JetExpression, expectedInfos: Collection<ExpectedInfo>, smartCastCalculator: SmartCastCalculator) {
        if (shouldCompleteThisItems(prefixMatcher)) {
            val items = thisExpressionItems(bindingContext, place, prefixMatcher.getPrefix(), resolutionFacade)
            for (item in items) {
                val types = smartCastCalculator.types(item.receiverParameter).map { FuzzyType(it, emptyList()) }
                val classifier = { expectedInfo: ExpectedInfo -> types.classifyExpectedInfo(expectedInfo) }
                addLookupElements(null, expectedInfos, classifier) {
                    item.createLookupElement().assignSmartCompletionPriority(SmartCompletionItemPriority.THIS).singletonList()
                }
            }
        }
    }

    private fun calcExpectedInfos(expression: JetExpression): Collection<ExpectedInfo> {
        // if our expression is initializer of implicitly typed variable - take type of variable from original file (+ the same for function)
        val declaration = implicitlyTypedDeclarationFromInitializer(expression)
        if (declaration != null) {
            val originalDeclaration = toFromOriginalFileMapper.toOriginalFile(declaration)
            if (originalDeclaration != null) {
                val originalDescriptor = originalDeclaration.resolveToDescriptor() as? CallableDescriptor
                val returnType = originalDescriptor?.getReturnType()
                if (returnType != null && !returnType.isError) {
                    return listOf(ExpectedInfo(returnType, declaration.getName(), null))
                }
            }
        }

        // if expected types are too general, try to use expected type from outer calls
        var count = 0
        while (true) {
            val infos = ExpectedInfos(bindingContext, resolutionFacade, useOuterCallsExpectedTypeCount = count)
                    .calculate(expression)
            if (count == 2 /* use two outer calls maximum */ || infos.none { it.fuzzyType?.isAlmostEverything() ?: false }) {
                return if (forBasicCompletion)
                    infos.map { it.copy(tail = null) }
                else
                    infos
            }
            count++
        }
        //TODO: we could always give higher priority to results with outer call expected type used
    }

    private fun implicitlyTypedDeclarationFromInitializer(expression: JetExpression): JetDeclaration? {
        val parent = expression.getParent()
        when (parent) {
            is JetVariableDeclaration -> if (expression == parent.getInitializer() && parent.getTypeReference() == null) return parent
            is JetNamedFunction -> if (expression == parent.getInitializer() && parent.getTypeReference() == null) return parent
        }
        return null
    }

    private fun toFunctionReferenceLookupElement(descriptor: DeclarationDescriptor,
                                                 functionExpectedInfos: Collection<ExpectedInfo>): LookupElement? {
        if (functionExpectedInfos.isEmpty()) return null

        fun toLookupElement(descriptor: FunctionDescriptor): LookupElement? {
            val functionType = functionType(descriptor) ?: return null

            val matchedExpectedInfos = functionExpectedInfos.filter { it.matchingSubstitutor(functionType) != null }
            if (matchedExpectedInfos.isEmpty()) return null

            var lookupElement = lookupElementFactory.createLookupElement(descriptor, useReceiverTypes = false)
            val text = "::" + (if (descriptor is ConstructorDescriptor) descriptor.getContainingDeclaration().getName() else descriptor.getName())
            lookupElement = object: LookupElementDecorator<LookupElement>(lookupElement) {
                override fun getLookupString() = text
                override fun getAllLookupStrings() = setOf(text)

                override fun renderElement(presentation: LookupElementPresentation) {
                    super.renderElement(presentation)
                    presentation.setItemText(text)
                    presentation.clearTail()
                    presentation.setTypeText(null)
                }

                override fun handleInsert(context: InsertionContext) {
                }
            }

            return lookupElement
                    .assignSmartCompletionPriority(SmartCompletionItemPriority.FUNCTION_REFERENCE)
                    .addTailAndNameSimilarity(matchedExpectedInfos)
        }

        if (descriptor is SimpleFunctionDescriptor) {
            return toLookupElement(descriptor)
        }
        else if (descriptor is ClassDescriptor && descriptor.getModality() != Modality.ABSTRACT) {
            val constructors = descriptor.getConstructors().filter(visibilityFilter)
            if (constructors.size() == 1) {
                //TODO: this code is to be changed if overloads to start work after ::
                return toLookupElement(constructors.single())
            }
        }

        return null
    }

    private fun buildForAsTypePosition(): Collection<LookupElement>? {
        val binaryExpression = ((expression.getParent() as? JetUserType)
                ?.getParent() as? JetTypeReference)
                    ?.getParent() as? JetBinaryExpressionWithTypeRHS
                        ?: return null
        val elementType = binaryExpression.getOperationReference().getReferencedNameElementType()
        if (elementType != JetTokens.AS_KEYWORD && elementType != JetTokens.AS_SAFE) return null
        val expectedInfos = calcExpectedInfos(binaryExpression)

        val expectedInfosGrouped: Map<JetType?, List<ExpectedInfo>> = expectedInfos.groupBy { it.fuzzyType?.type?.makeNotNullable() }

        val items = ArrayList<LookupElement>()
        for ((type, infos) in expectedInfosGrouped) {
            if (type == null) continue
            val lookupElement = lookupElementFactory.createLookupElementForType(type) ?: continue
            items.add(lookupElement.addTailAndNameSimilarity(infos))
        }
        return items
    }

    companion object {
        public val OLD_ARGUMENTS_REPLACEMENT_OFFSET: OffsetKey = OffsetKey.create("nonFunctionReplacementOffset")
        public val MULTIPLE_ARGUMENTS_REPLACEMENT_OFFSET: OffsetKey = OffsetKey.create("multipleArgumentsReplacementOffset")
    }
}
