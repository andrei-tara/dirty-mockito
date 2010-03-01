/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created Mar 1, 2010
 */
package dirty.mockito;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Base class for ActiveTest and MagicTest. Just provides a {@link #determineTypeParameter()} helper method that allows
 * for run-time parameterized type discovery.
 * 
 * @param <T>
 *        a type
 * @author Alistair A. Israel
 */
public abstract class ParameterizedTest<T> {

    /**
     * @return the Type parameter to our generic base class
     */
    @SuppressWarnings("unchecked")
    protected final Class<T> determineTypeParameter() {
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

}
