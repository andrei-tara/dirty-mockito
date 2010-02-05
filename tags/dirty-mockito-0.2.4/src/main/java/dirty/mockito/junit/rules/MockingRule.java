/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created Aug 14, 2009
 */
package dirty.mockito.junit.rules;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * <p>
 * A base class for JUnit {@link org.junit.Rule}s that construct (mock) object
 * test scaffolding.
 * </p>
 *
 * @author Alistair A. Israel
 * @since 0.2
 */
public abstract class MockingRule implements MethodRule {

    /**
     * {@inheritDoc}
     *
     * @see org.junit.rules.MethodRule#apply(org.junit.runners.model.Statement,
     *      org.junit.runners.model.FrameworkMethod, java.lang.Object)
     */
    public final Statement apply(final Statement base,
            final FrameworkMethod method, final Object target) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                initMocks(target);
                base.evaluate();
            }

        };
    }

    /**
     * Subclasses should override this to perform actual mocking.
     *
     * @param target
     *        the object we're constructing our mocks for
     */
    protected abstract void initMocks(final Object target);
}
