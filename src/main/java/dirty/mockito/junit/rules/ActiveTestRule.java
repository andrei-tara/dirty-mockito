/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created Aug 14, 2009
 */
package dirty.mockito.junit.rules;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.persistence.EntityManager;

import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;

import dirty.mockito.spring.context.TestBeanFactory;
import dirty.mockito.spring.jpa.MockEntityManagerFactory;
import dirty.mockito.utils.Reflection;

/**
 * <p>
 * JUnit 4.7 {@link org.junit.Rule} that performs instantiation, mocking and
 * injection of mocks. Uses reflection to instantiate objects, Mockito to
 * perform mocking of elements annotated with <a href=
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
 * <code>ActiveTestRule</code>, we simply add a {@link org.junit.Rule}
 * <code>ActiveTestRule&lt;WidgetDao&gt;</code>, declare a field of type
 * <code>WidgetDao</code>, declare another field of type
 * <code>Entitymanager</code> and annotate that using <code>&#064;Mock</code>.
 *
 * <pre>
 * public class WidgetDaoTest {
 *     &#064;Rule
 *     public ActiveTestRule&lt;WidgetDao&gt; activeTestRule = ActiveTestRule
 *             .thatWorksOn(WidgetDao.class);
 *
 *     &#064;Mock
 *     private EntityManager em;
 *
 *     private WidgetDao widgetDao;
 *
 *     &#064;Test
 *     public void testFind() {
 *         widgetDao.find(123L);
 *         verify(em).find(Entity.class, 123L);
 *     }
 * }
 * </pre>
 * <p>
 * That's it. ActiveTestRule takes care of creating the mocks for fields
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
public class ActiveTestRule<T> extends MockingRule {

    private final DefaultListableBeanFactory beanFactory = new TestBeanFactory();

    private final Class<T> classUnderTest;

    /**
     * @param classUnderTest
     *        the class under test
     */
    protected ActiveTestRule(final Class<T> classUnderTest) {
        this.classUnderTest = classUnderTest;
    }

    /**
     * @param <T>
     *        the type of the class under test
     * @param classUnderTest
     *        the class under test
     * @return an ActiveTestRule
     */
    public static <T> ActiveTestRule<T> thatWorksOn(
            final Class<T> classUnderTest) {
        return new ActiveTestRule<T>(classUnderTest);
    }

    /**
     * {@inheritDoc}
     *
     * @see dirty.mockito.junit.rules.MockingRule#initMocks(java.lang.Object)
     */
    @Override
    public final void initMocks(final Object unitTest) {
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
        final AnnotationEngine annotationEngine = new GlobalConfiguration()
                .getAnnotationEngine();
        final Field[] fields = clazz.getDeclaredFields();
        for (final Field field : fields) {
            for (final Annotation annotation : field.getAnnotations()) {
                final Object mock = annotationEngine.createMockFor(annotation,
                        field);
                if (mock != null) {
                    try {
                        Reflection.set(field).of(unitTest).to(mock);
                    } catch (final IllegalAccessException e) {
                        throw new MockitoException(
                                "Problems initializing fields annotated with "
                                        + annotation, e);
                    }

                    if (mock instanceof EntityManager) {
                        registerMockEntityManagerFactory((EntityManager) mock);
                    }
                    beanFactory.registerSingleton(field.getName(), mock);
                }
            }
        }
    }

    /**
     * @param em
     *        the (mock) {@link EntityManager}
     */
    private void registerMockEntityManagerFactory(final EntityManager em) {
        final MockEntityManagerFactory mockEntityManagerFactory = new MockEntityManagerFactory(
                em);
        beanFactory.registerSingleton("entityManagerFactory",
                mockEntityManagerFactory);

        final PersistenceAnnotationBeanPostProcessor pabpp = new PersistenceAnnotationBeanPostProcessor();
        pabpp.setBeanFactory(beanFactory);
        beanFactory.addBeanPostProcessor(pabpp);

    }

    /**
     * @param target
     *        the JUnit test we're intercepting
     */
    @SuppressWarnings("unchecked")
    private void instantiateObjectToTest(final Object target) {
        for (final Field field : target.getClass().getDeclaredFields()) {
            if (field.getType().equals(classUnderTest)) {

                final T object;
                if (JpaDaoSupport.class.isAssignableFrom(classUnderTest)) {
                    final String beanName = field.getName();
                    registerJpaDaoBeanDefinition(beanName);
                    object = (T) beanFactory.getBean(beanName, classUnderTest);
                } else {
                    object = (T) beanFactory.createBean(classUnderTest,
                            DefaultListableBeanFactory.AUTOWIRE_CONSTRUCTOR,
                            true);
                }
                try {
                    Reflection.set(field).of(target).to(object);
                } catch (final IllegalAccessException e) {
                    throw new MockitoException(
                            "Problems instantiating test object", e);
                }
            }
        }
    }

    /**
     * @param beanName
     *        the bean name to register
     */
    private void registerJpaDaoBeanDefinition(final String beanName) {
        final GenericBeanDefinition def = new GenericBeanDefinition();
        def.setBeanClass(classUnderTest);
        final MutablePropertyValues propertyValues = new MutablePropertyValues();
        final Object emf = beanFactory.getSingleton("entityManagerFactory");
        propertyValues.addPropertyValue("entityManagerFactory", emf);
        def.setPropertyValues(propertyValues);
        beanFactory.registerBeanDefinition(beanName, def);
    }

}
