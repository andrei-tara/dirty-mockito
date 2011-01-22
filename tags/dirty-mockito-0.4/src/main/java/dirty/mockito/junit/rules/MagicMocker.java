/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created Aug 14, 2009
 */
package dirty.mockito.junit.rules;

import org.mockito.MockitoAnnotations;

/**
 * <p>
 * A simple {@link MockingRule} that delegates to
 * {@link MockitoAnnotations#initMocks(Object)} to initialize fields (in the
 * unit test class) annotated with {@link org.mockito.Mock}. Let's you write,
 * for example:
 * </p>
 *
 * <pre>
 * public class MyTest {
 *
 *     &#064;Rule
 *     public MagicMocker mockitoRule = new MagicMocker();
 *
 *     &#064;Mock
 *     private Foo mock;
 *
 *     &#064;Test
 *     public void testSomething() {
 *         assertNotNull(mock);
 *         Something something = new Something(mock);
 *         // ...
 *     }
 * </pre>
 *
 * @author Alistair A. Israel
 * @since 0.2
 */

public class MagicMocker extends MockingRule {

    /**
     * {@inheritDoc}
     *
     * @see dirty.mockito.junit.rules.MockingRule#initMocks(java.lang.Object)
     */
    @Override
    protected final void initMocks(final Object target) {
        MockitoAnnotations.initMocks(target);
    }

}
