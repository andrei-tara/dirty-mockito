/**
 * Copyright (c) 2010 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created May 23, 2010
 */
package dirty.mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

/**
 * @author Alistair A. Israel
 */
public final class MagicTestTest {

    private static boolean wasRun;

    /**
     *
     */
    @Test
    public void testExtendsMagicTest() {
        wasRun = false;
        final Result result = JUnitCore.runClasses(ExtendsMagicTest.class);
        assertTrue(wasRun);
        assertEquals(0, result.getFailureCount());
    }

    /**
     * The unit test for {@link ClassUnderTest}, that extends {@link MagicTest}
     */
    public static final class ExtendsMagicTest extends MagicTest<ClassUnderTest> {

        private ClassUnderTest classUnderTest;

        /**
        *
        */
        @Test
        public void testMagic() {
            wasRun = true;
            assertNotNull(classUnderTest);
        }

    }

    /**
     * The class under test, which does nothing, really.
     */
    public static class ClassUnderTest {

    }

}
