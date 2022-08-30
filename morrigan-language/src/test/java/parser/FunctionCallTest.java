package parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class FunctionCallTest extends AbstractInterpreterTest {

  @Test
  public void shouldNotWork() {
    morrigan.interpret("morrigan remembers what is concat(1, 2).");
  }

  @Test
  public void shouldNotStackWhenRightBracketIsMissing() {
    assertThrows(Throwable.class, () -> {
      morrigan.interpret("morrigan remembers what is concat(1, 2");
    });
  }

  @Test
  public void nativeFunctionCall() {
    morrigan.interpret("morrigan says that a is [Hello]. morrigan says that b is [World]. "
        + "morrigan says that c is concat(a, b). ");
    assertThat(morrigan.getInterpreter().getEnvironment().getVariableValue("c")).isEqualTo("HelloWorld");
  }

  @Test
  public void functionDeclarationTest() {
    morrigan.interpret("morrigan says that helloWorld is function() {"
        + "morrigan remembers what is [Hello, World]}.");
  }

  @Test
  public void voidFunctionCallTest() {
    morrigan.interpret("morrigan says that helloWorld is function() {"
        + "morrigan says that a is 5}. morrigan remembers what is helloWorld().");
    assertThat(morrigan.getInterpreter().getEnvironment().getVariableValue("a")).isNull();
  }

  @Test
  public void helloWorldFunctionTest() {
    morrigan.interpret("morrigan says that helloWorld is function() {"
        + "morrigan remembers what is [Hello, world!]}. "
        + "morrigan remembers what is helloWorld().");
  }


  @Test
  public void overlappingScopesTest() {
    morrigan.interpret("morrigan says that a is 10. "
        + "morrigan says that test is function() {"
        + "morrigan says that a is 5 and morrigan remembers what is a"
        + "}."
        + "morrigan remembers what is test()."
        + "morrigan remembers what is a.");
  }

  @Test
  public void morriganCallTest() {
    morrigan.interpret("morrigan says that a is 5."
        + "morrigan says that t is function() {"
        + "morrigan remembers what is a}."
        + "morrigan calls t().");
  }

  @Test
  public void returnTest() {
    morrigan.interpret("morrigan says that a is function() {morrigan returns 5}."
        + "morrigan says that b is a().");
    assertThat(morrigan.getInterpreter().getEnvironment().getVariableValue("b")).isEqualTo(5);
  }

  @Test
  public void someMaxFunctionTest() {
    morrigan.interpret("morrigan says that a is 10."
        + "morrigan says that b is 15."
        + "morrigan says that max is function(num1, num2) { "
        + "  morrigan says that if num1 > num2 then morrigan returns num1 else"
        + "  morrigan returns num2"
        + "}."
        + "morrigan says that result is max(a, b)."
        + "morrigan remembers what is result.");
    assertThat(morrigan.getInterpreter().getEnvironment().getVariableValue("result")).
        isEqualTo(15);
  }

  @Test
  public void factorialTest() {
    morrigan.interpret("morrigan says that n is 5."
        + "morrigan says that f is function(n) { "
        + "morrigan says that if n = 1 then morrigan returns 1 else morrigan returns n * f(n - 1)}."
        + "morrigan says that a is f(n). morrigan remembers what is a.");
  }
}
