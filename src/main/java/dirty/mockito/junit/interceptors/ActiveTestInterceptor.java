/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created Aug 14, 2009
 */
package dirty.mockito.junit.interceptors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import dirty.mockito.utils.Reflection;

/**
 * <p>
 * JUnit 4.7 interceptor {@link org.junit.Rule} that performs instantiation,
 * mocking and injection of mocks. Uses reflection to instantiate objects,
 * Mockito to perform mocking of elements annotated with <a href=
 * "http://mockito.googlecode.com/svn/branches/1.6/javadoc/org/mockito/Mock.html"
 * ><code>&#064;Mock</code></a>, and an internal Spring BeanFactory to perform
 * injection of mocks into fields (in the object under test) annotated with
 * <code>&#064;Autowired</code>.
 * </p>
 * <p>
 * Suppose we have a JPA DAO class, that depends upon an
 * <code>&#064;Autowired</code> <code>EntityManager</code>:
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
 * <code>ActiveTestInterceptor</code>, we simply add a {@link org.junit.Rule}
 * <code>ActiveTestInterceptor&lt;WidgetDao&gt;</code>, declare a field of type
 * <code>WidgetDao</code>, declare another field of type
 * <code>Entitymanager</code> and annotate that using <code>&#064;Mock</code>.
 *
 * <pre>
 * public class WidgetDaoTest {
 *     &#064;Rule
 *     public ActiveTestInterceptor&lt;WidgetDao&gt; interceptor = ActiveTestInterceptor
 *             .thatWorksOn(WidgetDao.class);
 *     private WidgetDao widgetDao;
 *     &#064;Mock
 *     private EntityManager em;
 *
 *     &#064;Test
 *     public void testFind() {
 *         widgetDao.find(123L);
 *         Mockito.verify(em).find(Entity.class, 123L);
 *     }
 * }
 * </pre>
 * <p>
 * That's it. ActiveTestInterceptor takes care of creating the mocks for fields
 * annotated <code>&#064;Mock</code>, instantiating the class under test (
 * <code>WidgetDao</code>), and injecting the mock objects into
 * <code>&#064;Autowired</code> fields.
 * </p>
 * <p>
 * Because Spring itself is used to inject <code>&#064;Autowired</code>
 * dependencies, <code>ActiveTest</code> supports field, setter and
 * constructor-based injection.
 * </p>
 *
 * @param <T>
 *        the type (class) under test
 * @author Alistair A. Israel
 * @see <a href="http://code.google.com/p/dirty-mockito/" target="_blan
 *      >dirty-mockito</a>
 * @see <a href="http://static.springsource.org/spring/docs/2.5.x/api/org/springframework/beans/factory/annotation/Autowired.html"
 *      target="_blank>&#064;Autowired</a>
 * @see <a href=
 *      "http://mockito.googlecode.com/svn/branches/1.6/javadoc/org/mockito/Mock.html"
 *      target="_blank>&#064;Mock</a>
 * @since 0.2
 */
public class ActiveTestInterceptor<T> extends MockingInterceptor {

    private final DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();

    private final Class<T> classUnderTest;

    /**
     * @param classUnderTest
     *        the class under test
     */
    protected ActiveTestInterceptor(final Class<T> classUnderTest) {
        this.classUnderTest = classUnderTest;
        final AutowiredAnnotationBeanPostProcessor aabpp = new AutowiredAnnotationBeanPostProcessor();
        aabpp.setBeanFactory(defaultListableBeanFactory);
        defaultListableBeanFactory.addBeanPostProcessor(aabpp);
    }

    /**
     * @param <T>
     *        the type of the class under test
     * @param classUnderTest
     *        the class under test
     * @return an ActiveTestInterceptor
     */
    public static <T> ActiveTestInterceptor<T> thatWorksOn(final Class<T> classUnderTest) {
        return new ActiveTestInterceptor<T>(classUnderTest);
    }

    /**
     * {@inheritDoc}
     *
     * @see dirty.mockito.junit.interceptors.MockingInterceptor#initMocks(java.lang.Object)
     */
    @Override
    protected final void initMocks(final Object unitTest) {
        initializeMocks(unitTest);
        instantiateObjectToTest(unitTest);
    }

    /**
     * @param unitTest
     *        the JUnit test we're intercepting
     */
    private void initializeMocks(final Object unitTest) {
        Class<?> clazz = unitTest.getClass();
        while (clazz != Object.class) {
            scan(unitTest, clazz);
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * @param unitTest
     *        the JUnit test we're intercepting
     * @param clazz
     *        the class we're scanning
     */
    private void scan(final Object unitTest, final Class<?> clazz) {
        final AnnotationEngine annotationEngine = new GlobalConfiguration().getAnnotationEngine();
        final Field[] fields = clazz.getDeclaredFields();
        for (final Field field : fields) {
            for (final Annotation annotation : field.getAnnotations()) {
                final Object mock = annotationEngine.createMockFor(annotation, field);
                if (mock != null) {
                    try {
                        Reflection.set(field).of(unitTest).to(mock);
                    } catch (final IllegalAccessException e) {
                        throw new MockitoException("Problems initializing fields annotated with "
                                + annotation, e);
                    }
                    defaultListableBeanFactory.registerSingleton(field.getName(), mock);
                }
            }
        }
    }

    /**
     * @param target
     *        the JUnit test we're intercepting
     */
    @SuppressWarnings("unchecked")
    private void instantiateObjectToTest(final Object target) {
        for (final Field field : target.getClass().getDeclaredFields()) {
            if (field.getType().equals(classUnderTest)) {
                final T object = (T) defaultListableBeanFactory.createBean(classUnderTest);
                try {
                    Reflection.set(field).of(target).to(object);
                } catch (final IllegalAccessException e) {
                    throw new MockitoException("Problems instantiating test object", e);
                }
            }
        }
    }

}
