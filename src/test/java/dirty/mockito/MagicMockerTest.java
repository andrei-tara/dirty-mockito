/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created May 12, 2009
 */
package dirty.mockito;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mock;

/**
 * JUnit test for {@link MagicMocker}.
 *
 * @author Alistair A. Israel
 */
public class MagicMockerTest extends MagicMocker {

    @Mock
    private Foo mockFoo;

    /**
     * Test for {@link MagicMocker#setupMocks()}.
     */
    @Test
    public final void testSetupMocks() {
        assertNotNull(mockFoo);
        final UsesFoo usesFoo = new UsesFoo(mockFoo);
        usesFoo.fooBar();
        verify(mockFoo).bar();
    }

    /**
     * Just so we have something to mock.
     *
     * @author Alistair A. Israel
     */
    public static interface Foo {

        /**
         * Bar
         */
        void bar();
    }

    /**
     * Just so we have something to call the mock.
     *
     * @author Alistair A. Israel
     */
    public static final class UsesFoo {
        private final Foo foo;

        /**
         * @param foo
         *            {@link Foo}
         */
        public UsesFoo(final Foo foo) {
            this.foo = foo;
        }

        /**
         * @see dirty.mockito.MagicMockerTest.Foo#bar()
         */
        public void fooBar() {
            foo.bar();
        }

    }
}
