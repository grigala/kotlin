/*
 * Copyright 2010-2017 JetBrains s.r.o.
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

package org.jetbrains.kotlin.idea.completion.test;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.test.KotlinTestUtils;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.TestsPackage}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("idea/idea-completion/testData/basic/multifile")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class MultiFileJvmBasicCompletionTestGenerated extends AbstractMultiFileJvmBasicCompletionTest {
    public void testAllFilesPresentInMultifile() throws Exception {
        KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/idea-completion/testData/basic/multifile"), Pattern.compile("^([^\\.]+)$"), TargetBackend.ANY, false);
    }

    @TestMetadata("CallableReferenceNotImported")
    public void testCallableReferenceNotImported() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/CallableReferenceNotImported/");
        doTest(fileName);
    }

    @TestMetadata("CallableReferenceNotImportedExtension")
    public void testCallableReferenceNotImportedExtension() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/CallableReferenceNotImportedExtension/");
        doTest(fileName);
    }

    @TestMetadata("CallableReferenceNotImportedExtension2")
    public void testCallableReferenceNotImportedExtension2() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/CallableReferenceNotImportedExtension2/");
        doTest(fileName);
    }

    @TestMetadata("CallablesInExcludedPackage")
    public void testCallablesInExcludedPackage() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/CallablesInExcludedPackage/");
        doTest(fileName);
    }

    @TestMetadata("ClassInExcludedPackage")
    public void testClassInExcludedPackage() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/ClassInExcludedPackage/");
        doTest(fileName);
    }

    @TestMetadata("CompleteFunctionWithNoSpecifiedType")
    public void testCompleteFunctionWithNoSpecifiedType() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/CompleteFunctionWithNoSpecifiedType/");
        doTest(fileName);
    }

    @TestMetadata("CompleteImportedFunction")
    public void testCompleteImportedFunction() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/CompleteImportedFunction/");
        doTest(fileName);
    }

    @TestMetadata("CompletionOnImportedFunction")
    public void testCompletionOnImportedFunction() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/CompletionOnImportedFunction/");
        doTest(fileName);
    }

    @TestMetadata("DoNotCompleteWithConstraints")
    public void testDoNotCompleteWithConstraints() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/DoNotCompleteWithConstraints/");
        doTest(fileName);
    }

    @TestMetadata("EntriesOfNotImportedEnumFromKotlin")
    public void testEntriesOfNotImportedEnumFromKotlin() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/EntriesOfNotImportedEnumFromKotlin/");
        doTest(fileName);
    }

    @TestMetadata("EnumEntry")
    public void testEnumEntry() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/EnumEntry/");
        doTest(fileName);
    }

    @TestMetadata("ExactMatchPreferImported")
    public void testExactMatchPreferImported() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/ExactMatchPreferImported/");
        doTest(fileName);
    }

    @TestMetadata("ExcludedClass")
    public void testExcludedClass() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/ExcludedClass/");
        doTest(fileName);
    }

    @TestMetadata("ExcludedJavaClass")
    public void testExcludedJavaClass() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/ExcludedJavaClass/");
        doTest(fileName);
    }

    @TestMetadata("ExtensionFunction")
    public void testExtensionFunction() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/ExtensionFunction/");
        doTest(fileName);
    }

    @TestMetadata("ExtensionFunctionOnImportedFunction")
    public void testExtensionFunctionOnImportedFunction() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/ExtensionFunctionOnImportedFunction/");
        doTest(fileName);
    }

    @TestMetadata("ExtensionOnNullable")
    public void testExtensionOnNullable() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/ExtensionOnNullable/");
        doTest(fileName);
    }

    @TestMetadata("ExtensionsAndGetPrefix")
    public void testExtensionsAndGetPrefix() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/ExtensionsAndGetPrefix/");
        doTest(fileName);
    }

    @TestMetadata("ExtensionsForSmartCast")
    public void testExtensionsForSmartCast() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/ExtensionsForSmartCast/");
        doTest(fileName);
    }

    @TestMetadata("FileRefInStringLiteral")
    public void testFileRefInStringLiteral() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/FileRefInStringLiteral/");
        doTest(fileName);
    }

    @TestMetadata("FileRefInStringLiteralNoPrefix")
    public void testFileRefInStringLiteralNoPrefix() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/FileRefInStringLiteralNoPrefix/");
        doTest(fileName);
    }

    @TestMetadata("GroovyClassNameCompletionFromDefaultPackage")
    public void testGroovyClassNameCompletionFromDefaultPackage() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/GroovyClassNameCompletionFromDefaultPackage/");
        doTest(fileName);
    }

    @TestMetadata("GroovyClassNameCompletionFromNonDefaultPackage")
    public void testGroovyClassNameCompletionFromNonDefaultPackage() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/GroovyClassNameCompletionFromNonDefaultPackage/");
        doTest(fileName);
    }

    @TestMetadata("HiddenDeclarations")
    public void testHiddenDeclarations() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/HiddenDeclarations/");
        doTest(fileName);
    }

    @TestMetadata("InImport")
    public void testInImport() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/InImport/");
        doTest(fileName);
    }

    @TestMetadata("InImportedFunctionLiteralParameter")
    public void testInImportedFunctionLiteralParameter() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/InImportedFunctionLiteralParameter/");
        doTest(fileName);
    }

    @TestMetadata("IncorrectGetters")
    public void testIncorrectGetters() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/IncorrectGetters/");
        doTest(fileName);
    }

    @TestMetadata("JavaCallableReference")
    public void testJavaCallableReference() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/JavaCallableReference/");
        doTest(fileName);
    }

    @TestMetadata("JavaInnerClasses")
    public void testJavaInnerClasses() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/JavaInnerClasses/");
        doTest(fileName);
    }

    @TestMetadata("KT12124")
    public void testKT12124() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/KT12124/");
        doTest(fileName);
    }

    @TestMetadata("KT9835")
    public void testKT9835() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/KT9835/");
        doTest(fileName);
    }

    @TestMetadata("MoreSpecificExtensionGeneric")
    public void testMoreSpecificExtensionGeneric() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/MoreSpecificExtensionGeneric/");
        doTest(fileName);
    }

    @TestMetadata("MoreSpecificExtensionInDifferentPackage")
    public void testMoreSpecificExtensionInDifferentPackage() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/MoreSpecificExtensionInDifferentPackage/");
        doTest(fileName);
    }

    @TestMetadata("MoreSpecificExtensionIsPrivate")
    public void testMoreSpecificExtensionIsPrivate() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/MoreSpecificExtensionIsPrivate/");
        doTest(fileName);
    }

    @TestMetadata("NoAutoInsertionOfNotImported")
    public void testNoAutoInsertionOfNotImported() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/NoAutoInsertionOfNotImported/");
        doTest(fileName);
    }

    @TestMetadata("NoExtForOuterFromNested")
    public void testNoExtForOuterFromNested() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/NoExtForOuterFromNested/");
        doTest(fileName);
    }

    @TestMetadata("NoExtensionMethodDuplication")
    public void testNoExtensionMethodDuplication() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/NoExtensionMethodDuplication/");
        doTest(fileName);
    }

    @TestMetadata("NoGenericFunDuplication")
    public void testNoGenericFunDuplication() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/NoGenericFunDuplication/");
        doTest(fileName);
    }

    @TestMetadata("NotImportedExtensionForImplicitReceiver")
    public void testNotImportedExtensionForImplicitReceiver() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/NotImportedExtensionForImplicitReceiver/");
        doTest(fileName);
    }

    @TestMetadata("NotImportedExtensionFunction")
    public void testNotImportedExtensionFunction() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/NotImportedExtensionFunction/");
        doTest(fileName);
    }

    @TestMetadata("NotImportedExtensionFunction2")
    public void testNotImportedExtensionFunction2() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/NotImportedExtensionFunction2/");
        doTest(fileName);
    }

    @TestMetadata("NotImportedExtensionFunction3")
    public void testNotImportedExtensionFunction3() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/NotImportedExtensionFunction3/");
        doTest(fileName);
    }

    @TestMetadata("NotImportedExtensionFunctionAndAlias")
    public void testNotImportedExtensionFunctionAndAlias() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/NotImportedExtensionFunctionAndAlias/");
        doTest(fileName);
    }

    @TestMetadata("NotImportedExtensionProperty")
    public void testNotImportedExtensionProperty() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/NotImportedExtensionProperty/");
        doTest(fileName);
    }

    @TestMetadata("NotImportedFunction")
    public void testNotImportedFunction() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/NotImportedFunction/");
        doTest(fileName);
    }

    @TestMetadata("NotImportedInfixExtension")
    public void testNotImportedInfixExtension() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/NotImportedInfixExtension/");
        doTest(fileName);
    }

    @TestMetadata("NotImportedJavaClass")
    public void testNotImportedJavaClass() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/NotImportedJavaClass/");
        doTest(fileName);
    }

    @TestMetadata("NotImportedObject")
    public void testNotImportedObject() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/NotImportedObject/");
        doTest(fileName);
    }

    @TestMetadata("NotImportedProperty")
    public void testNotImportedProperty() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/NotImportedProperty/");
        doTest(fileName);
    }

    @TestMetadata("ObjectInTypePosition")
    public void testObjectInTypePosition() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/ObjectInTypePosition/");
        doTest(fileName);
    }

    @TestMetadata("ObjectMembers")
    public void testObjectMembers() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/ObjectMembers/");
        doTest(fileName);
    }

    @TestMetadata("ParameterNameAndTypeForNotImportedAlias")
    public void testParameterNameAndTypeForNotImportedAlias() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/ParameterNameAndTypeForNotImportedAlias/");
        doTest(fileName);
    }

    @TestMetadata("ParameterNameAndTypeNestedClasses")
    public void testParameterNameAndTypeNestedClasses() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/ParameterNameAndTypeNestedClasses/");
        doTest(fileName);
    }

    @TestMetadata("PreferMemberToExtension")
    public void testPreferMemberToExtension() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/PreferMemberToExtension/");
        doTest(fileName);
    }

    @TestMetadata("PreferMemberToGlobal")
    public void testPreferMemberToGlobal() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/PreferMemberToGlobal/");
        doTest(fileName);
    }

    @TestMetadata("PreferMoreSpecificExtension1")
    public void testPreferMoreSpecificExtension1() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/PreferMoreSpecificExtension1/");
        doTest(fileName);
    }

    @TestMetadata("PreferMoreSpecificExtension2")
    public void testPreferMoreSpecificExtension2() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/PreferMoreSpecificExtension2/");
        doTest(fileName);
    }

    @TestMetadata("PreferMoreSpecificExtension3")
    public void testPreferMoreSpecificExtension3() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/PreferMoreSpecificExtension3/");
        doTest(fileName);
    }

    @TestMetadata("PropertyKeysEmptyString")
    public void testPropertyKeysEmptyString() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/PropertyKeysEmptyString/");
        doTest(fileName);
    }

    @TestMetadata("PropertyKeysNoPrefix")
    public void testPropertyKeysNoPrefix() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/PropertyKeysNoPrefix/");
        doTest(fileName);
    }

    @TestMetadata("PropertyKeysWithPrefix")
    public void testPropertyKeysWithPrefix() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/PropertyKeysWithPrefix/");
        doTest(fileName);
    }

    @TestMetadata("StaticMembersOfNotImportedClassFromJava")
    public void testStaticMembersOfNotImportedClassFromJava() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/StaticMembersOfNotImportedClassFromJava/");
        doTest(fileName);
    }

    @TestMetadata("StaticMembersOfNotImportedClassFromKotlin")
    public void testStaticMembersOfNotImportedClassFromKotlin() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/StaticMembersOfNotImportedClassFromKotlin/");
        doTest(fileName);
    }

    @TestMetadata("StaticMembersOfNotImportedClassFromKotlinObject")
    public void testStaticMembersOfNotImportedClassFromKotlinObject() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/StaticMembersOfNotImportedClassFromKotlinObject/");
        doTest(fileName);
    }

    @TestMetadata("StaticMembersOfNotImportedClassNameConflict")
    public void testStaticMembersOfNotImportedClassNameConflict() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/StaticMembersOfNotImportedClassNameConflict/");
        doTest(fileName);
    }

    @TestMetadata("SyntheticExtensionDeprecated")
    public void testSyntheticExtensionDeprecated() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/SyntheticExtensionDeprecated/");
        doTest(fileName);
    }

    @TestMetadata("SyntheticExtensionForGenericClass")
    public void testSyntheticExtensionForGenericClass() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/SyntheticExtensionForGenericClass/");
        doTest(fileName);
    }

    @TestMetadata("SyntheticExtensionNonVoidSetter")
    public void testSyntheticExtensionNonVoidSetter() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/SyntheticExtensionNonVoidSetter/");
        doTest(fileName);
    }

    @TestMetadata("TopLevelFunction")
    public void testTopLevelFunction() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/TopLevelFunction/");
        doTest(fileName);
    }

    @TestMetadata("TypeAliases")
    public void testTypeAliases() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/idea-completion/testData/basic/multifile/TypeAliases/");
        doTest(fileName);
    }
}
