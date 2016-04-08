# Mockito straight up #

If you just want a little convenience with mocking objects using Mockito, then just use the `MagicMocker` rule.

```
public class UsesMockResourceTest {

  @Rule
  public MagicMocker magicMocker = new MagicMocker();

  @Mock
  private Resource mockResource;

  private UsesResource usesResource;

  @Before
  public void setUp() {
    usesResource = new UsesResource(mockResource);
  }

  @Test
  public void testUseResource() {
    usesResource.useResource();

    assertTrue(mockResource.isUsed());

    verify(mockResource).use();
  }
```

**NOTE:** JUnit 4.7 executes `@Before` methods _before_ `@Rule`s. Hence, any `@Mock`s initialized by `MagicMocker` will be unavailable in a test's `@Before` methods. The above code sample will only work properly on JUnit 4.8 or higher.