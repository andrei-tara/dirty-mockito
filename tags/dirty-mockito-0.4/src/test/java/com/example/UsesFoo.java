/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created May 29, 2009
 */
package com.example;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Just so we have something to call the mock.
 *
 * @author Alistair A. Israel
 */
public final class UsesFoo {

    @Autowired
    private Foo foo;

    /**
     * Default constructor
     */
    public UsesFoo() {
        // noop
    }

    /**
     * @param foo
     *            {@link Foo}
     */
    public UsesFoo(final Foo foo) {
        this.foo = foo;
    }

    /**
     * @return the foo
     */
    public Foo getFoo() {
        return foo;
    }

    /**
     * @param foo
     *            the foo to set
     */
    public void setFoo(final Foo foo) {
        this.foo = foo;
    }

    /**
     * @see com.example.Foo#bar()
     */
    public void fooBar() {
        foo.bar();
    }

}
