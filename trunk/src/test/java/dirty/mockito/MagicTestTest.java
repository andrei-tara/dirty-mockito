/**
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
public class MagicTestTest {

    private static boolean wasRun;

    public static class ClassUnderTest {

    }

    public static class ExtendsMagicTest extends MagicTest<ClassUnderTest> {

        private ClassUnderTest classUnderTest;

        @Test
        public void testMagic() {
            wasRun = true;
            assertNotNull(classUnderTest);
        }

    }

    @Test
    public void testExtendsMagicTest() {
        wasRun = false;
        final Result result = JUnitCore.runClasses(ExtendsMagicTest.class);
        assertTrue(wasRun);
        assertEquals(0, result.getFailureCount());
    }
}
