/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created May 12, 2009
 */
package dirty.mockito;

import org.junit.Rule;

import dirty.mockito.junit.interceptors.MockitoInterceptor;

/**
 * <p>
 * A convenience base class for JUnit + Mockito tests. Lets you write, for
 * example:
 * </p>
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
 * mocked using a {@link MockitoInterceptor}.
 *
 * @author Alistair A. Israel
 * @see <a href=
 *      "http://mockito.googlecode.com/svn/branches/1.6/javadoc/org/mockito/Mock.html"
 *      target="_blank>&#064;Mock</a>
 * @since 0.1
 */
// CHECKSTYLE:OFF
public class MagicMocker {
    // CHECKSTYLE:ON

    /**
     *
     */
    @Rule
    // CHECKSTYLE:OFF
    public final MockitoInterceptor mockitoInterceptor = new MockitoInterceptor();
    // CHECKSTYLE:ON

}
