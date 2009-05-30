/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created May 28, 2009
 */
package dirty.mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Test;

/**
 * Ostensibly a test for {@link Adder}, but actually exercises {@link dirty.mockito.MagicTest}.
 *
 * @author Alistair A. Israel
 */
public final class AdderTest extends MagicTest<Adder> {

    private Adder adder;

    /**
     *
     */
    @Test
    public void testActiveTest() {
        assertEquals(0, adder.getDelta());
        assertEquals(Adder.class, getClassUnderTest());
        assertNotNull(adder);
        adder.setDelta(1);
        assertEquals(2, adder.add(1));
    }


    /**
     *
     */
    @Test
    public void testActiveTestTwo() {
        assertEquals(0, adder.getDelta());
        assertEquals(Adder.class, getClassUnderTest());
        Assert.assertNotNull(adder);
        adder.setDelta(2);
        assertEquals(4, adder.add(2));
    }
}
