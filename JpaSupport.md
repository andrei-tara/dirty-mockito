`dirty-mockito` supports JPA `EntityManager` mocking and injection.

Suppose you start with the following unit test:
```
public class WidgetDaoTest extends ActiveTest<WidgetDao> {

  @Mock
  private EntityManager em;

  @Mock
  private Query query;

  private WidgetDao widgetDao;

  @Test
  public void testFindByName() {
    Widget widget = new Widget();

    when(em.createNamedQuery("WidgetDao.findByName")).thenReturn(query);
    when(query.getResultList()).thenReturn(Collections.singletonList(widget));

    assertSame(widget, widgetDao.findByName("Bob"));

    verify(query).setParameter("name", "Bob");
  }

}
```

`WidgetDao` can be written four ways:

### Using constructor injection ###
```
public class WidgetDao {

  private final EntityManager em;

  public WidgetDao(EntityManager em) {
    this.em = em;
  }

  public Widget findByName(String name) {
    Query query = em.createNamedQuery("Widget.findByName");
    query.setParameter("name", name);
    List results = query.getResultList();
    if (results.isEmpty()) {
      return null;
    }
    return results.get(0);
  }
```

### Using `@Autowired` setter (or field) injection ###
```
public class WidgetDao {

  private EntityManager em;

  @Autowired
  public void setEntityManager(EntityManager em) {
    this.em = em;
  }

  ...
```

### Using JPA `@PersistenceContext` ###
```
public class WidgetDao {

  @PersistenceContext
  private EntityManager em;

  ...
```

### Using Spring `JpaDaoSupport`-derived classes ###
```
public class WidgetDao extends JpaDaoSupport {

  public Widget findByName(String name) {
    Map params = Collections.singletonMap("name", name);
    List results = getJpaTemplate().findByNamedQueryAndNamedParams("WidgetDao.findById", params);
    ...
```

In any of the above cases, `dirty-mockito` will inject the mock `EntityManager` properly to the JPA DAO.