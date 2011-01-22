/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created May 28, 2009
 */
package dirty.mockito;

import java.lang.reflect.Field;

import org.junit.Before;

import dirty.mockito.utils.Reflection;

/**
 * A 'proof of concept' for {@link dirty.mockito.ActiveTest}.
 *
 * @param <T>
 *        the type under test
 * @author Alistair A. Israel
 * @since 0.1
 */
public class MagicTest<T> extends ParameterizedTest<T> {

    private final Class<T> classUnderTest;

    /**
     *
     */
    public MagicTest() {
        this.classUnderTest = determineTypeParameter();
    }

    /**
     * @throws Exception
     *         on exception
     */
    @Before
    public final void instantiateObjectToTest() throws Exception {
        for (final Field field : this.getClass().getDeclaredFields()) {
            if (field.getType().equals(classUnderTest)) {
                final T object = classUnderTest.newInstance();
                Reflection.set(field).of(this).to(object);
            }
        }
    }

    /**
     * @return the classUnderTest
     */
    public final Class<T> getClassUnderTest() {
        return classUnderTest;
    }

}
