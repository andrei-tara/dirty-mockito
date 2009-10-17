/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created May 12, 2009
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
 * JUnit test for {@link dirty.mockito.MagicMocker}.
 *
 * @author Alistair A. Israel
 */
public class MagicMockerTest {

    /**
     * {@link MagicMocker} does all the magic.
     */
    @Rule
    // CHECKSTYLE:OFF
    public final MagicMocker magicMocker = new MagicMocker();
    // CHECKSTYLE:ON

    @Mock
    private Foo mockFoo;

    /**
     * Test for {@link dirty.mockito.MagicMocker#setupMocks()}.
     */
    @Test
    public final void testSetupMocks() {
        assertNotNull(mockFoo);
        final UsesFoo usesFoo = new UsesFoo(mockFoo);
        usesFoo.fooBar();
        verify(mockFoo).bar();
    }
}
