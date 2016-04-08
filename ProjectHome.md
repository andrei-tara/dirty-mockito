**NOTICE** Due to Google Code's imminent retirement, I've moved this project over to  https://github.com/aisrael/dirty-mockito/

JUnit has been refined over time. Mockito tastes really well. Spring is very refreshing.

`dirty-mockito`, on the other hand is not for everybody. Nevertheless, we hope some of you might like it.

# Announcing `dirty-mockito 0.3` #
I'm glad to announce the release of `dirty-mockito 0.3`. This release updates its dependency on `junit` to `4.8.1` (now that it's finally up on Maven central).

With `junit 4.8.1`, `@Rule`s now execute before `@Before` methods. This now allows `ActiveTest` to simply use `ActiveTestRule`. Also, I've eliminated a little redundancy by moving the `determineTypeParameter()` method present in both `ActiveTest` and `MagicTest` into a common base class, `ParameterizedTest`. (_2010-03-01_)

# `dirty-mockito 0.2.4` released #
This will hopefully be the final 0.2.x release before 0.3.x, which will use JUnit 4.8 (once that's on the Maven repos). This release basically just renames "interceptors" to "rules".

# `dirty-mockito 0.2.3` released #
This is just a minor revision that basically 'reverts' to the old-style `@Before` mocking and instantiation in `ActiveTest`. This is to 'workaround' the fact that in JUnit 4.7, test `@Before` methods run _before_ `@Rule`s. This release will once again ensure that mocked/instantiated fields will be accessible in `@Before` methods.

The next release of JUnit will execute `@Rule`s before `@Before` methods, which will let `ActiveTest` use the `ActiveTestInterceptor` as a proper `@Rule` once more.

Also, we now only register a `PersistenceContextBeanPostProcessor` _if_ an `EntityManager` has been `@Mock`ed. That is, it allows you to write a test for a class that contains a `@PersistenceContext` even if your test does not declare a `@Mock` `EntityManager`. Of course, it'll be up to you to provide the rest of the dependencies. For example,
```
public class WidgetServiceTest extends ActiveTest<WidgetService> {

  @Mock
  private WidgetDao widgetDao;

  private WidgetService widgetService;

  @Before
  public void setMockDaoManually() {
    widgetService.setWidgetDao(widgetDao);
  }

  ...
```

Will now work properly, whereas before Spring would complain open seeing a `@PersistenceContext` annotation (in `WidgetService`) that an `EntityManager` or `EntityManagerFactory` was not registered.

# `dirty-mockito 0.2.2` now supports mock `EntityManager` injection to JPA DAOs #
`dirty-mockito 0.2.2` has just been released. This version adds transparent support for JPA data access objects (DAO), using various Spring bean post-processors and an internal, custom `EntityManagerFactory`.

JPA DAO classes can be written three ways:

### Using JPA `@PersistenceContext` ###
It is possible to declare a Java EE 5 style DAO as follows:
```
public class WidgetDao {

  @PersistenceContext
  private EntityManager em;

  ...
```
### Using constructor injection ###
```
public class WidgetDao {

  private final EntityManager em;

  public WidgetDao(EntityManager em) {
    this.em = em;
  }

  ...
```
### Using Spring `JpaDaoSupport`-derived classes ###
```
public class WidgetDao extends JpaDaoSupport {

  ...
```

## Usage ##
To use `dirty-mockito` in your Maven projects, simply add the following to the appropriate sections of your `pom.xml`:
```
<project>

  <dependencies>

    <dependency>
      <groupId>dirty-mockito</groupId>
      <artifactId>dirty-mockito</artifactId>
      <version>0.3</version>
      <scope>test</scope>
    </dependency>

  </dependencies>

  <repositories>

    <repository>
      <id>dirty-mockito</id>
      <url>http://dirty-mockito.googlecode.com/svn/maven2/releases</url>
    </repository>

  </repositories>

</project>
```

## Disclaimer ##

`dirty-mockito` is NOT affiliated with the Mockito project, NOR with the JUnit project, NOR the Spring Framework project.