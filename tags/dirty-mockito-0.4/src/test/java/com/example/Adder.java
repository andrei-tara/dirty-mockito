/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created May 28, 2009
 */
package com.example;

/**
 * A basic class to test.
 *
 * @author Alistair A. Israel
 */
public class Adder {

    private int delta;

    /**
     * @return the delta
     */
    public final int getDelta() {
        return delta;
    }

    /**
     * @param delta
     *            the delta to set
     */
    public final void setDelta(final int delta) {
        this.delta = delta;
    }

    /**
     * @param x
     *            int
     * @return x + delta
     */
    public final int add(final int x) {
        return x + delta;
    }

}
