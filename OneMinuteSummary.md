# Instantiation, mocking and injection for the lazy #

`dirty-mockito` is a set of unofficial extensions that blend together the [JUnit](http://www.junit.org/) testing framework, the [Mockito](http://mockito.org/) mocking framework with a dash of [Spring](http://www.springframework.org) and then some.

Suppose we have an interface:
```
public interface Foo {

  void bar();

}
```
And a class that uses that interface:
```
public class UsesFoo {

  @Autowired
  private Foo foo;

  public Foo getFoo() {
    return foo;
  }

  public void fooBar() {
    foo.bar();
  }
}
```
Then `dirty-mockito` lets us write tests by simply going:
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