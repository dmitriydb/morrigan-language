package ru.shanalotte.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import ru.shanalotte.environment.Environment;

public class FunctionCallTest extends AbstractInterpreterTest{

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
    assertThat(Environment.getGlobalVariableValue("c")).isEqualTo("HelloWorld");
  }
}
