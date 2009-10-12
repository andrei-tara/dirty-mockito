/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created May 28, 2009
 */
package dirty.mockito;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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
public class MagicTest<T> {

    private final Class<T> classUnderTest;

    /**
     *
     */
    public MagicTest() {
        this.classUnderTest = determineTypeParameter();
    }

    /**
     * @return the Type parameter to our generic base class
     */
    @SuppressWarnings("unchecked")
    private Class<T> determineTypeParameter() {
        Class<?> specificClass = this.getClass();
        Type genericSuperclass = specificClass.getGenericSuperclass();
        while (!(genericSuperclass instanceof ParameterizedType) && specificClass != MagicTest.class) {
            specificClass = specificClass.getSuperclass();
            genericSuperclass = specificClass.getGenericSuperclass();
        }
        final ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;

        final Type firstTypeParameter = parameterizedType.getActualTypeArguments()[0];
        return (Class<T>) firstTypeParameter;
    }

    /**
     * @throws Exception
     *         on exception
     */
    @Before
    public final void instantiateObjectToTest() throws Exception {
        final MagicTest<T> target = this;
        for (final Field field : target.getClass().getDeclaredFields()) {
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
