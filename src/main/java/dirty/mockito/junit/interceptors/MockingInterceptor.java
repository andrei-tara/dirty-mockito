/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created Aug 14, 2009
 */
package dirty.mockito.junit.interceptors;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * A base class for JUnit interceptors ({@link org.junit.Rule}) that construct
 * (mock) object test harnesses.
 *
 * @author Alistair A. Israel
 * @since 0.2
 */
public abstract class MockingInterceptor implements MethodRule {

    /**
     * {@inheritDoc}
     *
     * @see org.junit.rules.MethodRule#apply(org.junit.runners.model.Statement,
     *      org.junit.runners.model.FrameworkMethod, java.lang.Object)
     */
    public final Statement apply(final Statement base, final FrameworkMethod method, final Object target) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                initMocks(target);
                base.evaluate();
            }

        };
    }

    /**
     * @param target
     *        the object we're constructing our mocks for
     */
    protected abstract void initMocks(final Object target);
}
