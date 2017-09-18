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

package org.jetbrains.kotlin.idea.spring.tests.references;

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
@TestMetadata("ultimate/testData/spring/core/references/completion/variants")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class SpringReferenceCompletionTestGenerated extends AbstractSpringReferenceCompletionTest {
    public void testAllFilesPresentInVariants() throws Exception {
        KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("ultimate/testData/spring/core/references/completion/variants"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
    }

    @TestMetadata("beanWithDefaultName.kt")
    public void testBeanWithDefaultName() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("ultimate/testData/spring/core/references/completion/variants/beanWithDefaultName.kt");
        doTest(fileName);
    }

    @TestMetadata("beanWithExplicitName.kt")
    public void testBeanWithExplicitName() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("ultimate/testData/spring/core/references/completion/variants/beanWithExplicitName.kt");
        doTest(fileName);
    }

    @TestMetadata("packageReference.kt")
    public void testPackageReference() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("ultimate/testData/spring/core/references/completion/variants/packageReference.kt");
        doTest(fileName);
    }

    @TestMetadata("qualifierReference1Xml.kt")
    public void testQualifierReference1Xml() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("ultimate/testData/spring/core/references/completion/variants/qualifierReference1Xml.kt");
        doTest(fileName);
    }

    @TestMetadata("qualifierReference2Xml.kt")
    public void testQualifierReference2Xml() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("ultimate/testData/spring/core/references/completion/variants/qualifierReference2Xml.kt");
        doTest(fileName);
    }

    @TestMetadata("scopeReferenceXml.kt")
    public void testScopeReferenceXml() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("ultimate/testData/spring/core/references/completion/variants/scopeReferenceXml.kt");
        doTest(fileName);
    }

    @TestMetadata("springBeanReferenceXml.kt")
    public void testSpringBeanReferenceXml() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("ultimate/testData/spring/core/references/completion/variants/springBeanReferenceXml.kt");
        doTest(fileName);
    }
}
