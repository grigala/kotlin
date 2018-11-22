/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.navigation;

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
@TestMetadata("idea/testData/navigation/relatedSymbols/multiModule")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class KotlinGotoRelatedSymbolMultiModuleTestGenerated extends AbstractKotlinGotoRelatedSymbolMultiModuleTest {
    private void runTest(String testDataFilePath) throws Exception {
        KotlinTestUtils.runTest(this::doTest, TargetBackend.ANY, testDataFilePath);
    }

    public void testAllFilesPresentInMultiModule() throws Exception {
        KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/testData/navigation/relatedSymbols/multiModule"), Pattern.compile("^([^\\.]+)$"), TargetBackend.ANY, false);
    }

    @TestMetadata("fromActualMemberFunToExpect")
    public void testFromActualMemberFunToExpect() throws Exception {
        runTest("idea/testData/navigation/relatedSymbols/multiModule/fromActualMemberFunToExpect/");
    }

    @TestMetadata("fromActualMemberValToExpect")
    public void testFromActualMemberValToExpect() throws Exception {
        runTest("idea/testData/navigation/relatedSymbols/multiModule/fromActualMemberValToExpect/");
    }

    @TestMetadata("fromExpectMemberFunToActuals")
    public void testFromExpectMemberFunToActuals() throws Exception {
        runTest("idea/testData/navigation/relatedSymbols/multiModule/fromExpectMemberFunToActuals/");
    }

    @TestMetadata("fromExpectMemberValToActuals")
    public void testFromExpectMemberValToActuals() throws Exception {
        runTest("idea/testData/navigation/relatedSymbols/multiModule/fromExpectMemberValToActuals/");
    }

    @TestMetadata("fromNestedActualClassToExpect")
    public void testFromNestedActualClassToExpect() throws Exception {
        runTest("idea/testData/navigation/relatedSymbols/multiModule/fromNestedActualClassToExpect/");
    }

    @TestMetadata("fromNestedExpectClassToActuals")
    public void testFromNestedExpectClassToActuals() throws Exception {
        runTest("idea/testData/navigation/relatedSymbols/multiModule/fromNestedExpectClassToActuals/");
    }

    @TestMetadata("fromTopLevelActualClassToExpect")
    public void testFromTopLevelActualClassToExpect() throws Exception {
        runTest("idea/testData/navigation/relatedSymbols/multiModule/fromTopLevelActualClassToExpect/");
    }

    @TestMetadata("fromTopLevelActualFunToExpect")
    public void testFromTopLevelActualFunToExpect() throws Exception {
        runTest("idea/testData/navigation/relatedSymbols/multiModule/fromTopLevelActualFunToExpect/");
    }

    @TestMetadata("fromTopLevelActualValToExpect")
    public void testFromTopLevelActualValToExpect() throws Exception {
        runTest("idea/testData/navigation/relatedSymbols/multiModule/fromTopLevelActualValToExpect/");
    }

    @TestMetadata("fromTopLevelExpectClassToActuals")
    public void testFromTopLevelExpectClassToActuals() throws Exception {
        runTest("idea/testData/navigation/relatedSymbols/multiModule/fromTopLevelExpectClassToActuals/");
    }

    @TestMetadata("fromTopLevelExpectFunToActuals")
    public void testFromTopLevelExpectFunToActuals() throws Exception {
        runTest("idea/testData/navigation/relatedSymbols/multiModule/fromTopLevelExpectFunToActuals/");
    }

    @TestMetadata("fromTopLevelExpectValToActuals")
    public void testFromTopLevelExpectValToActuals() throws Exception {
        runTest("idea/testData/navigation/relatedSymbols/multiModule/fromTopLevelExpectValToActuals/");
    }
}
