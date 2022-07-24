package ru.shanalotte.parser;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import ru.shanalotte.Morrigan;
import ru.shanalotte.environment.Environment;

public class StatementsTest extends AbstractInterpreterTest{

  @Test
  public void firstStatement() {
    runSomeCode("morrigan remembers what is 2 * 4 * 11.");
  }

  @Test
  public void firstSettingVariable() {
    runSomeCode("morrigan says that a is 2 * 4 * 11.");
    runSomeCode("morrigan remembers what is a.");
  }

  @Test
  public void usingVariableInExpressions() {
    runSomeCode("morrigan says that a is 3. morrigan says that b is a + 7. morrigan remembers what is b.");
    assertThat(Environment.getGlobalVariableValue("b")).isEqualTo(10);
  }

  @Test
  public void assigningSomeBooleanValues() {
    runSomeCode("morrigan says that tenMoreThatFive is 10 > 5. ");
    assertThat(Environment.getGlobalVariableValue("tenMoreThatFive")).isEqualTo(true);
  }

  @Test
  public void addTwoNumbers() {
    runSomeCode("morrigan says that a is 5. morrigan says that b is 5. morrigan says that c is a + b.");
    assertThat(Environment.getGlobalVariableValue("c")).isEqualTo(10);
  }

  @Test
  public void addTwoCentsValues() {
    runSomeCode("morrigan says that firstCentsAmount is 11. morrigan says that secondCentsAmount is 22. morrigan says that totalCents is firstCentsAmount + secondCentsAmount. morrigan remembers what is 0 + o + totalCents.");

  }

  private void runSomeCode(String s) {
    String code = s;
    morrigan.interpret(code);
  }
}
