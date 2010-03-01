/**
 * Copyright (c) 2009 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created May 29, 2009
 */
package dirty.mockito;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Rule;

import dirty.mockito.junit.rules.ActiveTestRule;

/**
 * <p>
 * JUnit 4 test parameterized base class that performs instantiation, mocking and injection of mocks. Uses an internal
 * {@link ActiveTestRule} to perform mocking of elements annotated with <a href=
 * "http://mockito.googlecode.com/svn/branches/1.6/javadoc/org/mockito/Mock.html" ><code>&#064;Mock</code></a> then
 * inject those mocks into fields (in the object under test) annotated with <code>&#064;Autowired</code>.
 * </p>
 * <p>
 * Suppose we have a JPA DAO class, that depends upon an <code>&#064;Autowired</code> <code>EntityManager</code>:
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
 * Now, to write a unit test for <code>WidgetDao</code> using <code>ActiveTest</code>, we simply extend
 * <code>ActiveTest&lt;WidgetDao&gt;</code>, declare a field of type <code>WidgetDao</code>, declare another field of
 * type <code>Entitymanager</code> and annotate that using <code>&#064;Mock</code>.
 * 
 * <pre>
 * public class WidgetDaoTest extends ActiveTest&lt;WidgetDao&gt; {
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
 * That's it. ActiveTest takes care of creating the mocks for fields annotated <code>&#064;Mock</code>, instantiating
 * the class under test ( <code>WidgetDao</code>), and injecting the mock objects into <code>&#064;Autowired</code>
 * fields.
 * </p>
 * <p>
 * Because Spring itself is used to inject <code>&#064;Autowired</code> dependencies, <code>ActiveTest</code> supports
 * field, setter and constructor-based injection.
 * </p>
 * 
 * @param <T>
 *        the type of the class to instantiate and inject mocks into
 * @author Alistair A. Israel
 * @see <a href="http://code.google.com/p/dirty-mockito/" target="_blan >dirty-mockito</a>
 * @see <a href=
 *      "http://static.springsource.org/spring/docs/2.5.x/api/org/springframework/beans/factory/annotation/Autowired.html"
 *      target="_blank>&#064;Autowired</a>
 * @see <a href= "http://mockito.googlecode.com/svn/branches/1.6/javadoc/org/mockito/Mock.html"
 *      target="_blank>&#064;Mock</a>
 * @since 0.1
 */
public class ActiveTest<T> {

    /**
     * The {@link ActiveTestRule} that does all the mojo.
     */
    @Rule
    // CHECKSTYLE:OFF
    public final ActiveTestRule<T> activeTestrule;
    // CHECKSTYLE:ON

    /**
     *
     */
    public ActiveTest() {
        final Class<T> classUnderTest = determineTypeParameter();
        this.activeTestrule = ActiveTestRule.thatWorksOn(classUnderTest);
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
}
