/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created May 12, 2009
 */
package dirty.mockito;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

/**
 * <p>
 * A convenience base class for JUnit + Mockito tests. Lets you write, for
 * example:
 * </p>
 *
 * <pre>
 * public class MyTest extends MagicMocker {
 *     &#064;Mock
 *     private Foo mock;
 *     &#064;Test
 *     public void testSomething() {
 *         Something something = new Something(mock);
 *         // ...
 *     }
 * </pre>
 * <p>
 * Where the fields annotated with {@link org.mockito.Mock} are automatically
 * mocked using {@link MockitoAnnotations#initMocks(Object)}.
 *
 * @author Alistair A. Israel
 */
public class MagicMocker {

    /**
     * Called by the JUnit test runner before any subclass @Before methods,
     * setupMocks() uses MockitoAnnotations.initMocks() on the actual test class
     * to search for and mock any fields annotated with {@link org.mockito.Mock}
     * .
     *
     * @see MockitoAnnotations#initMocks(Object)
     */
    @Before
    public final void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }
}
