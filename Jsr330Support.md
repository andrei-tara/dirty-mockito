# Support for JSR-330 annotations #

Since `dirty-mockito` uses Spring 3.x, then JSR-330 `@Inject` annotations are automatically supported. Using the same examples from the OneMinuteSummary:

If we have a class under test that declares a dependency using `@Inject`:
```
public class UsesFoo {

  @Inject
  private Foo foo;

  public Foo getFoo() {
    return foo;
  }

  public void fooBar() {
    foo.bar();
  }
}
```
Then the following test should work as expected:
```
public class UsesFooTest extends ActiveTest<UsesFoo> {

  private UsesFoo usesFoo;

  @Mock
  private Foo mockFoo;

  @Test
  public void testFooBar() {
    assertNotNull(mockFoo);
    assertNotNull(usesFoo);
    assertSame(mockFoo, usesFoo.getFoo());
    usesFoo.fooBar();
    verify(mockFoo).bar();
  }
}
```