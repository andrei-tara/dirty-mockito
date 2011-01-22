/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created Aug 14, 2009
 */
package dirty.mockito.junit.rules;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import com.example.Foo;
import com.example.UsesFoo;


/**
 * JUnit test for {@link ActiveTestRule}.
 *
 * @author Alistair A. Israel
 */
public class ActiveTestRuleTest {

    /**
     *
     */
    @Rule
    // SUPPRESS CHECKSTYLE VisibilityModifier
    public final ActiveTestRule<UsesFoo> activeTestRule = ActiveTestRule
            .thatWorksOn(UsesFoo.class);

    private UsesFoo usesFoo;

    @Mock
    private Foo mockFoo;

    /**
     * Test method for {@link com.example.UsesFoo#fooBar()}.
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
