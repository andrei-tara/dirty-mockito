/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created Aug 14, 2009
 */
package dirty.mockito.junit.rules;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import dirty.mockito.Foo;
import dirty.mockito.UsesFoo;

/**
 * JUnit test that tests and illustrates the use of {@link MockitoRule}.
 *
 * @author Alistair A. Israel
 */
public final class MockitoRuleTest {

    /**
     * Use a {@link MockitoRule} to mock our fields annotated with
     * {@link Mock}.
     */
    @Rule
    // CHECKSTYLE:OFF
    public final MockitoRule mockitoRule = new MockitoRule();
    // CHECKSTYLE:ON

    @Mock
    private Foo mockFoo;

    /**
     * At this point, our MockitoRule should've kicked in and mocked
     * <code>mockFoo</code>.
     */
    @Test
    public void testUsesMockFoo() {
        assertNotNull(mockFoo);
        final UsesFoo usesFoo = new UsesFoo(mockFoo);
        usesFoo.fooBar();
        verify(mockFoo).bar();
    }
}
