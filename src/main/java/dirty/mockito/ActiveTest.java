/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created May 29, 2009
 */
package dirty.mockito;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Before;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * <p>
 * JUnit 4 test parameterized base class that performs instantiation, mocking
 * and injection of mocks. Uses reflection to instantiate objects, Mockito to
 * perform mocking of elements annotated with <a href=
 * "http://mockito.googlecode.com/svn/branches/1.6/javadoc/org/mockito/Mock.html"
 * ><code>@Mock</code></a>, and uses an internal Spring BeanFactory to perform
 * injection of mocks into elements annotated with
 * <a href="http://static.springsource.org/spring/docs/2.5.x/api/org/springframework/beans/factory/annotation/Autowired.html">.
 * </p>
 * <p>
 * Suppose we have a JPA DAO class, that depends upon an <code>@Autowired</code>
 * <code>EntityManager</code>:
 * </p>
 *
 * <pre>
 * public class WidgetDao {
 *     &#064;Autowired
 *     private EntityManager em;
 *
 *     public Widget find(final Long id) {
 *         return em.find(Widget.class, id);
 *     }
 * }
 * </pre>
 * <p>
 * Now, to write a unit test for <code>WidgetDao</code> using
 * <code>ActiveTest</code>, we simply extend
 * <code>ActiveTest&lt;WidgetDao&gt;</code>, declare a field of type
 * <code>WidgetDao</code>, declare another field of type
 * <code>Entitymanager</code> and annotate that using <code>@Mock</code>.
 *
 * <pre>
 * public class WidgetDaoTest extends ActiveTest&lt;WidgetDao&gt; {
 *   private WidgetDao widgetDao;
 *   &#064;Mock
 *   private EntityManager em;
 *   &#064;Test
 *   public void testFind() {
 *     widgetDao.find(123L);
 *     Mockito.verify(em).find(Entity.class, 123L);
 *   }
 * </pre>
 * <p>
 * That's it. ActiveTest takes care of creating the mocks for fields annotated
 * <code>@Mock</code>, instantiating the class under test (
 * <code>WidgetDao</code>), and injecting the mock objects into
 * <code>@Autowired</code> fields.
 * </p>
 * <p>
 * Because Spring itself is used to inject <code>@Autowired</code> dependencies,
 * <code>ActiveTest</code> supports field, setter and constructor-based
 * injection.
 * </p>
 *
 * @param <T>
 *            the type of the class to instantiate and inject mocks into
 * @author Alistair A. Israel
 * @see <a href=&quot;http://code.google.com/p/dirty-mockito/>dirty-mockito</a>;
 *      on Google Code
 * @since 0.1
 */
public class ActiveTest<T> {

    private final DefaultListableBeanFactory defaultListableBeanFactory =
            new DefaultListableBeanFactory();

    private final Class<T> classUnderTest;

    /**
     *
     */
    @SuppressWarnings("unchecked")
    public ActiveTest() {
        this.classUnderTest = (Class) determineTypeParameter();
        final AutowiredAnnotationBeanPostProcessor aabpp =
                new AutowiredAnnotationBeanPostProcessor();
        aabpp.setBeanFactory(defaultListableBeanFactory);
        defaultListableBeanFactory.addBeanPostProcessor(aabpp);
    }

    /**
     * @return the Type parameter to our generic base class
     */
    private Type determineTypeParameter() {
        Class<?> specificClass = this.getClass();
        Type genericSuperclass = specificClass.getGenericSuperclass();
        while (!(genericSuperclass instanceof ParameterizedType)
                && specificClass != MagicTest.class) {
            specificClass = specificClass.getSuperclass();
            genericSuperclass = specificClass.getGenericSuperclass();
        }
        final ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;

        final Type firstTypeParameter = parameterizedType.getActualTypeArguments()[0];
        return firstTypeParameter;
    }

    /**
     * @return the classUnderTest
     */
    public final Class<T> getClassUnderTest() {
        return classUnderTest;
    }

    /**
     *
     */
    @Before
    public final void initializeMocksAndInstantiateObject() {
        initializeMocks();
        instantiateObjectToTest();
    }

    /**
     *
     */
    private void initializeMocks() {
        Class<?> clazz = this.getClass();
        while (clazz != Object.class) {
            scan(clazz);
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * @param clazz
     *            the class we're scanning
     */
    private void scan(final Class<?> clazz) {
        final AnnotationEngine annotationEngine =
                new GlobalConfiguration().getAnnotationEngine();
        final Field[] fields = clazz.getDeclaredFields();
        for (final Field field : fields) {
            for (final Annotation annotation : field.getAnnotations()) {
                final Object mock = annotationEngine.createMockFor(annotation, field);
                if (mock != null) {
                    try {
                        setThisFieldTo(field, mock);
                        defaultListableBeanFactory.registerSingleton(field.getName(), mock);
                    } catch (final IllegalAccessException e) {
                        throw new MockitoException("Problems initiating mocks annotated with "
                                + annotation, e);
                    }
                }
            }
        }
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    public final void instantiateObjectToTest() {
        for (final Field field : this.getClass().getDeclaredFields()) {
            if (field.getType().equals(classUnderTest)) {
                try {
                    final T object = (T) defaultListableBeanFactory.createBean(classUnderTest);
                    setThisFieldTo(field, object);
                } catch (final IllegalAccessException e) {
                    throw new MockitoException("Problems instantiating test object", e);
                }
            }
        }
    }

    /**
     * @param field
     *            the field to set
     * @param value
     *            the value to set it to
     * @throws IllegalAccessException
     *             on exception
     */
    private void setThisFieldTo(final Field field, final Object value)
            throws IllegalAccessException {
        final boolean accessible = field.isAccessible();
        if (!accessible) {
            field.setAccessible(true);
        }
        field.set(this, value);
        if (!accessible) {
            field.setAccessible(false);
        }
    }

}
