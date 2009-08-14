/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created Aug 14, 2009
 */
package dirty.mockito.junit.interceptors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import dirty.mockito.Foo;
import dirty.mockito.UsesFoo;

/**
 *
 * @author Alistair A. Israel
 */
public class ActiveTestInterceptorTest {

    /**
     *
     */
    @Rule
    // CHECKSTYLE:OFF
    public final ActiveTestInterceptor<UsesFoo> activeTestInterceptor =
            ActiveTestInterceptor.thatWorksOn(UsesFoo.class);
    // CHECKSTYLE:ON

    private UsesFoo usesFoo;

    @Mock
    private Foo mockFoo;

    /**
     * Test method for {@link dirty.mockito.UsesFoo#fooBar()}.
     */
    @Test
    public final void testFooBar() {
        assertNotNull(mockFoo);
        assertNotNull(usesFoo);
        assertSame(mockFoo, usesFoo.getFoo());
        usesFoo.fooBar();
        verify(mockFoo).bar();
    }

}
