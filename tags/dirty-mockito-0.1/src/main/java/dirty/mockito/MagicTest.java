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

/**
 * A 'proof of concept' for {@link dirty.mockito.ActiveTest}.
 *
 * @param <T>
 *            the type under test
 * @author Alistair A. Israel
 * @since 0.1
 */
public class MagicTest<T> {

    private final Class<T> classUnderTest;

    /**
     *
     */
    @SuppressWarnings("unchecked")
    public MagicTest() {
        Class<?> specificClass = this.getClass();
        Type genericSuperclass = specificClass.getGenericSuperclass();
        while (!(genericSuperclass instanceof ParameterizedType)
                && specificClass != MagicTest.class) {
            specificClass = specificClass.getSuperclass();
            genericSuperclass = specificClass.getGenericSuperclass();
        }
        final ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;

        final Type firstTypeParameter = parameterizedType.getActualTypeArguments()[0];
        this.classUnderTest = (Class) firstTypeParameter;
    }

    /**
     * @throws Exception
     *             on exception
     */
    @Before
    public final void instantiateObjectToTest() throws Exception {
        for (final Field field : this.getClass().getDeclaredFields()) {
            if (field.getType().equals(classUnderTest)) {
                final T object = classUnderTest.newInstance();
                final boolean accessible = field.isAccessible();
                if (!accessible) {
                    field.setAccessible(true);
                }
                field.set(this, object);
                if (!accessible) {
                    field.setAccessible(false);
                }
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
