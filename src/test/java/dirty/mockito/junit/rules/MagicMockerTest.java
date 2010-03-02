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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import com.example.Foo;
import com.example.UsesFoo;

/**
 * JUnit test for {@link dirty.mockito.MagicMocker}.
 * 
 * @author Alistair A. Israel
 */
public final class MagicMockerTest {

    /**
     * {@link MagicMocker} does all the magic.
     */
    @Rule
    // CHECKSTYLE:OFF
    public final MagicMocker magicMocker = new MagicMocker();
    // CHECKSTYLE:ON

    @Mock
    private Foo mockFoo;

    private UsesFoo usesFoo;

    /**
     * Called before test methods
     */
    @Before
    public void setUp() {
        usesFoo = new UsesFoo(mockFoo);
    }

    /**
     * Test for {@link dirty.mockito.MagicMocker#setupMocks()}.
     */
    @Test
    public void testSetupMocks() {
        assertNotNull(mockFoo);
        usesFoo.fooBar();
        verify(mockFoo).bar();
    }
}
