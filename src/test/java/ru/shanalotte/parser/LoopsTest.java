package ru.shanalotte.parser;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import ru.shanalotte.environment.Environment;

public class LoopsTest extends AbstractInterpreterTest{

  @Test
  public void shouldIncrementVariableTenTimes() {
    morrigan.interpret("morrigan says that a is 0."
        + "morrigan says that a is a + 1 x 10.");
    assertThat(Environment.getGlobalVariableValue("a")).isEqualTo(10);
  }

  @Test
  public void shouldPrintTenTimes() {
    morrigan.interpret("morrigan says that a is helloworld. morrigan remembers what is a x 10.");
  }

  @Test
  public void whileEvaluationTest() {
    morrigan.interpret("morrigan says that a is 0. "
        + "morrigan says that while a < 10 morrigan says that a is a + 1.");
    assertThat(Environment.getGlobalVariableValue("a")).isEqualTo(10);
  }

  @Test
  public void tenIsNotLessThenTen() {
    morrigan.interpret("morrigan says that a is 10 < 10.");
    assertThat(Environment.getGlobalVariableValue("a")).isEqualTo(false);
  }

  @Test
  public void tenIsNotMoreThenTen() {
    morrigan.interpret("morrigan says that a is 10 > 10.");
    assertThat(Environment.getGlobalVariableValue("a")).isEqualTo(false);
  }

  @Test
  public void tenEqualsTen() {
    morrigan.interpret("morrigan says that a is 10 = 10.");
    assertThat(Environment.getGlobalVariableValue("a")).isEqualTo(true);
  }
}
