/**
 * Copyright (c) 2011 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created Jan 21, 2011
 */
package dirty.mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.mockito.Mock;

/**
 * JUnit test for {@link ActiveTest}
 *
 * @author Alistair A. Israel
 */
public final class ActiveTestTest {

    private static boolean wasRun;

    /**
     *
     */
    @Test
    public void testExtendsActiveTestSucceeds() {
        wasRun = false;
        final Result result = JUnitCore.runClasses(ExtendsActiveTest.class);
        assertTrue(wasRun);
        assertEquals(0, result.getFailureCount());
    }

    /**
     * The test for {@link ClassUnderTest}, that descends from
     * {@link ActiveTest}
     */
    public static final class ExtendsActiveTest extends ActiveTest<ClassUnderTest> {

        @Mock
        private SomeCollaborator mockCollaborator;

        private ClassUnderTest classUnderTest;

        /**
         * Test for {@link ClassUnderTest#foo()}
         */
        @Test
        public void testFoo() {
            assertNotNull(classUnderTest);
            assertNotNull(mockCollaborator);
            assertSame(mockCollaborator, classUnderTest.collaborator);

            classUnderTest.foo();

            verify(mockCollaborator).bar();
            wasRun = true;
        }

    }

    /**
     * The class under test.
     */
    public static final class ClassUnderTest {

        @Inject
        private SomeCollaborator collaborator;

        /**
         * Calls {@link SomeCollaborator#bar()}
         */
        public void foo() {
            collaborator.bar();
        }
    }


    /**
     * Some interface that the {@link ClassUnderTest} collaborates with.
     */
    public static interface SomeCollaborator {

        /**
         * bar()
         */
        void bar();
    }
}
