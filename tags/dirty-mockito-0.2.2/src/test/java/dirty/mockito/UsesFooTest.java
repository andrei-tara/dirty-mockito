/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created May 29, 2009
 */
package dirty.mockito;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mock;

/**
 * Ostenibly a test for {@link UsesFoo} but actually exercises {@link dirty.mockito.ActiveTest}.
 *
 * @author Alistair A. Israel
 */
public class UsesFooTest extends ActiveTest<UsesFoo> {

    private UsesFoo usesFoo;

    @Mock
    private Foo mockFoo;

    /**
     * Test method for {@link dirty.mockito.UsesFoo#fooBar()}.
     */
    @Test
    public final void testFooBar() {
        assertNotNull(usesFoo);
        assertNotNull(mockFoo);
        assertSame(mockFoo, usesFoo.getFoo());
        usesFoo.fooBar();
        verify(mockFoo).bar();
    }

}
