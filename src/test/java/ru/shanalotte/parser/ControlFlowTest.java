package ru.shanalotte.parser;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shanalotte.Morrigan;
import ru.shanalotte.environment.Environment;

public class ControlFlowTest extends AbstractInterpreterTest{

  @BeforeEach
  public void clearEnvronment() {
    Environment.clear();
  }

  @Test
  public void ifStatementTest() {
    morrigan.interpret("morrigan says that if 3 > 2 then morrigan says that a is 3.");
    assertThat(Environment.globalVariableExists("a"));
    assertThat(Environment.getGlobalVariableValue("a")).isEqualTo(3);
  }

  @Test
  public void ifStatementElseCaseTest() {
    morrigan.interpret("morrigan says that if 5 > 10 then morrigan says that a is 10 else morrigan says that a is 5.");
    assertThat(Environment.getGlobalVariableValue("a")).isEqualTo(5);
  }

  @Test
  public void maxFunctionImplementation() {
    morrigan.interpret("morrigan says that a is 5."
        + "morrigan says that b is 10."
        + "morrigan says that if a > b then morrigan says that max is a else morrigan says that max is b.");
    assertThat(Environment.getGlobalVariableValue("max")).isEqualTo(10);
  }
}
