/*
 * Copyright 2010-2013 JetBrains s.r.o.
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

package org.jetbrains.jet.checkers;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import kotlin.Function1;
import kotlin.KotlinPackage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.JetTestUtils;
import org.jetbrains.jet.cli.jvm.compiler.CliLightClassGenerationSupport;
import org.jetbrains.jet.lang.descriptors.*;
import org.jetbrains.jet.lang.descriptors.impl.ModuleDescriptorImpl;
import org.jetbrains.jet.lang.diagnostics.*;
import org.jetbrains.jet.lang.psi.Call;
import org.jetbrains.jet.lang.psi.JetElement;
import org.jetbrains.jet.lang.psi.JetExpression;
import org.jetbrains.jet.lang.psi.JetFile;
import org.jetbrains.jet.lang.resolve.*;
import org.jetbrains.jet.lang.resolve.calls.model.MutableResolvedCall;
import org.jetbrains.jet.lang.resolve.calls.model.ResolvedCall;
import org.jetbrains.jet.lang.resolve.java.TopDownAnalyzerFacadeForJVM;
import org.jetbrains.jet.lang.resolve.lazy.LazyResolveTestUtil;
import org.jetbrains.jet.lang.resolve.name.FqName;
import org.jetbrains.jet.lang.resolve.name.Name;
import org.jetbrains.jet.lang.types.lang.KotlinBuiltIns;
import org.jetbrains.jet.test.util.DescriptorValidator;
import org.jetbrains.jet.test.util.RecursiveDescriptorComparator;

import java.io.File;
import java.util.*;

import static org.jetbrains.jet.lang.diagnostics.Errors.*;
import static org.jetbrains.jet.test.util.RecursiveDescriptorComparator.RECURSIVE;

public abstract class AbstractJetDiagnosticsTest extends BaseDiagnosticsTest {
    @Override
    protected void analyzeAndCheck(File testDataFile, List<TestFile> testFiles) {
        Map<TestModule, List<TestFile>> groupedByModule = KotlinPackage.groupByTo(
                testFiles,
                new LinkedHashMap<TestModule, List<TestFile>>(),
                new Function1<TestFile, TestModule>() {
                    @Override
                    public TestModule invoke(TestFile file) {
                        return file.getModule();
                    }
                }
        );

        List<JetFile> allJetFiles = new ArrayList<JetFile>();
        Map<TestModule, ModuleDescriptorImpl> modules = createModules(groupedByModule);
        Map<TestModule, BindingContext> moduleBindings = new HashMap<TestModule, BindingContext>();

        for (Map.Entry<TestModule, List<TestFile>> entry : groupedByModule.entrySet()) {
            TestModule testModule = entry.getKey();
            List<? extends TestFile> testFilesInModule = entry.getValue();

            List<JetFile> jetFiles = getJetFiles(testFilesInModule);
            allJetFiles.addAll(jetFiles);

            ModuleDescriptorImpl module = modules.get(testModule);
            BindingTrace moduleTrace = new BindingTraceContext();

            moduleBindings.put(testModule, moduleTrace.getBindingContext());

            // New JavaDescriptorResolver is created for each module, which is good because it emulates different Java libraries for each module,
            // albeit with same class names
            TopDownAnalyzerFacadeForJVM.analyzeFilesWithJavaIntegration(
                    getProject(),
                    jetFiles,
                    Predicates.<PsiFile>alwaysTrue(),
                    module,
                    null,
                    null
            );

            checkAllResolvedCallsAreCompleted(jetFiles, moduleTrace.getBindingContext());
        }

        boolean ok = true;

        StringBuilder actualText = new StringBuilder();
        for (TestFile testFile : testFiles) {
            ok &= testFile.getActualText(moduleBindings.get(testFile.getModule()), actualText, groupedByModule.size() > 1);
        }

        JetTestUtils.assertEqualsToFile(testDataFile, actualText.toString());

        assertTrue("Diagnostics mismatch. See the output above", ok);

        // TODO:
        // checkAllResolvedCallsAreCompleted(allJetFiles, supportTrace.getBindingContext());

        validateAndCompareDescriptorWithFile(testDataFile, testFiles, modules);
    }

    private void validateAndCompareDescriptorWithFile(
            File testDataFile,
            List<TestFile> testFiles,
            Map<TestModule, ModuleDescriptorImpl> modules
    ) {
        StringBuilder rootPackageText = new StringBuilder();

        //ModuleDescriptorImpl lightClassModule = support.getLightClassModuleModule();
        //if (lightClassModule == null) {
        //    // FIXME: must be separate cli for each module analysis
        //    ModuleDescriptorImpl cliModule = support.newModule();
        //    cliModule.initialize(new PackageFragmentProvider() {
        //        @NotNull
        //        @Override
        //        public List<PackageFragmentDescriptor> getPackageFragments(@NotNull FqName fqName) {
        //            return Collections.emptyList();
        //        }
        //
        //        @NotNull
        //        @Override
        //        public Collection<FqName> getSubPackagesOf(@NotNull FqName fqName) {
        //            return Collections.emptyList();
        //        }
        //    });
        //}

        RecursiveDescriptorComparator comparator = new RecursiveDescriptorComparator(createdAffectedPackagesConfiguration(testFiles));

        boolean isMultiModuleTest = modules.size() != 1;

        for (TestModule module : Ordering.natural().sortedCopy(modules.keySet())) {
            ModuleDescriptorImpl moduleDescriptor = modules.get(module);
            DeclarationDescriptor aPackage = moduleDescriptor.getPackage(FqName.ROOT);

            if (isMultiModuleTest) {
                rootPackageText.append(String.format("// -- Module: %s --\n", moduleDescriptor.getName()));
            }

            String actualSerialized = comparator.serializeRecursively(aPackage);
            rootPackageText.append(actualSerialized);

            if (isMultiModuleTest) {
                rootPackageText.append("\n\n");
            }
        }

        String path = JetTestUtils
                .getFilePath(new File(FileUtil.getRelativePath(new File("compiler/testData/diagnostics/tests"), testDataFile)));
        String txtFileRelativePath = path.replaceAll("\\.kt$|\\.kts$", ".txt");
        File txtFile = new File("compiler/testData/lazyResolve/diagnostics/" + txtFileRelativePath);

        JetTestUtils.assertEqualsToFile(txtFile, rootPackageText.toString());
    }

    public static RecursiveDescriptorComparator.Configuration createdAffectedPackagesConfiguration(List<TestFile> testFiles) {
        final Set<Name> names = LazyResolveTestUtil.getTopLevelPackagesFromFileList(getJetFiles(testFiles));

        final Predicate<FqName> fqNameFilter = new Predicate<FqName>() {
            @Override
            public boolean apply(FqName fqName) {
                if (fqName.isRoot()) return true;

                Name firstName = fqName.pathSegments().get(0);
                return names.contains(firstName);
            }
        };

        return RECURSIVE.filterRecursion(fqNameFilter).withValidationStrategy(
                DescriptorValidator.ValidationVisitor.allowErrorTypes(
                        new Predicate<DeclarationDescriptor>() {
                            @Override
                            public boolean apply(DeclarationDescriptor input) {
                                if (input instanceof PackageViewDescriptor) {
                                    PackageViewDescriptor descriptor = (PackageViewDescriptor) input;
                                    FqName fqName = descriptor.getFqName();
                                    System.out.println(fqName);

                                    return fqNameFilter.apply(fqName);
                                }
                                return true;
                            }
                        }
                )
        );
    }

    public static Map<TestModule, ModuleDescriptorImpl> createModules(Map<TestModule, List<TestFile>> groupedByModule) {
        Map<TestModule, ModuleDescriptorImpl> modules = new HashMap<TestModule, ModuleDescriptorImpl>();

        for (TestModule testModule : groupedByModule.keySet()) {
            ModuleDescriptorImpl module =
                    testModule == null ?
                    TopDownAnalyzerFacadeForJVM.createAnalyzeModule() :
                    TopDownAnalyzerFacadeForJVM.createJavaModule("<" + testModule.getName() + ">");

            modules.put(testModule, module);
        }

        for (TestModule testModule : groupedByModule.keySet()) {
            if (testModule == null) continue;

            ModuleDescriptorImpl module = modules.get(testModule);
            module.addDependencyOnModule(module);
            for (TestModule dependency : testModule.getDependencies()) {
                module.addDependencyOnModule(modules.get(dependency));
            }
        }

        for (TestModule testModule : groupedByModule.keySet()) {
            if (testModule == null) continue;

            modules.get(testModule).addDependencyOnModule(KotlinBuiltIns.getInstance().getBuiltInsModule());
            modules.get(testModule).seal();
        }

        return modules;
    }

    private static void checkAllResolvedCallsAreCompleted(@NotNull List<JetFile> jetFiles, @NotNull BindingContext bindingContext) {
        for (JetFile file : jetFiles) {
            if (!AnalyzingUtils.getSyntaxErrorRanges(file).isEmpty()) {
                return;
            }
        }

        ImmutableMap<Call, ResolvedCall<?>> resolvedCallsEntries = bindingContext.getSliceContents(BindingContext.RESOLVED_CALL);
        for (Map.Entry<Call, ResolvedCall<?>> entry : resolvedCallsEntries.entrySet()) {
            JetElement element = entry.getKey().getCallElement();
            ResolvedCall<?> resolvedCall = entry.getValue();

            DiagnosticUtils.LineAndColumn lineAndColumn =
                    DiagnosticUtils.getLineAndColumnInPsiFile(element.getContainingFile(), element.getTextRange());

            assertTrue("Resolved call for '" + element.getText() + "'" + lineAndColumn + " is not completed",
                       ((MutableResolvedCall<?>) resolvedCall).isCompleted());
        }

        checkResolvedCallsInDiagnostics(bindingContext);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    private static void checkResolvedCallsInDiagnostics(BindingContext bindingContext) {
        Set<DiagnosticFactory1<PsiElement, Collection<? extends ResolvedCall<?>>>> diagnosticsStoringResolvedCalls1 = Sets.newHashSet(
                OVERLOAD_RESOLUTION_AMBIGUITY, NONE_APPLICABLE, CANNOT_COMPLETE_RESOLVE, UNRESOLVED_REFERENCE_WRONG_RECEIVER,
                ASSIGN_OPERATOR_AMBIGUITY, ITERATOR_AMBIGUITY);
        Set<DiagnosticFactory2<JetExpression, ? extends Comparable<? extends Comparable<?>>, Collection<? extends ResolvedCall<?>>>>
                diagnosticsStoringResolvedCalls2 = Sets.newHashSet(
                COMPONENT_FUNCTION_AMBIGUITY, DELEGATE_SPECIAL_FUNCTION_AMBIGUITY, DELEGATE_SPECIAL_FUNCTION_NONE_APPLICABLE);
        Diagnostics diagnostics = bindingContext.getDiagnostics();
        for (Diagnostic diagnostic : diagnostics) {
            DiagnosticFactory<?> factory = diagnostic.getFactory();
            //noinspection SuspiciousMethodCalls
            if (diagnosticsStoringResolvedCalls1.contains(factory)) {
                assertResolvedCallsAreCompleted(
                        diagnostic, DiagnosticFactory.cast(diagnostic, diagnosticsStoringResolvedCalls1).getA());

            }
            //noinspection SuspiciousMethodCalls
            if (diagnosticsStoringResolvedCalls2.contains(factory)) {
                assertResolvedCallsAreCompleted(
                        diagnostic,
                        DiagnosticFactory.cast(diagnostic, diagnosticsStoringResolvedCalls2).getB());
            }
        }
    }

    private static void assertResolvedCallsAreCompleted(
            @NotNull Diagnostic diagnostic, @NotNull Collection<? extends ResolvedCall<?>> resolvedCalls
    ) {
        boolean allCallsAreCompleted = true;
        for (ResolvedCall<?> resolvedCall : resolvedCalls) {
            if (!((MutableResolvedCall<?>) resolvedCall).isCompleted()) {
                allCallsAreCompleted = false;
            }
        }

        PsiElement element = diagnostic.getPsiElement();
        DiagnosticUtils.LineAndColumn lineAndColumn =
                DiagnosticUtils.getLineAndColumnInPsiFile(element.getContainingFile(), element.getTextRange());

        assertTrue("Resolved calls stored in " + diagnostic.getFactory().getName() + "\n" +
                   "for '" + element.getText() + "'" + lineAndColumn + " are not completed",
                   allCallsAreCompleted);
    }
}
