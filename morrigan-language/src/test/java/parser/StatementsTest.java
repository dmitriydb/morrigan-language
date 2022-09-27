package parser;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

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
    assertThat(morrigan.getInterpreter().getEnvironment().getVariableValue("b")).isEqualTo(10.0d);
  }

  @Test
  public void assigningSomeBooleanValues() {
    runSomeCode("morrigan says that tenMoreThatFive is 10 > 5. ");
    assertThat(morrigan.getInterpreter().getEnvironment().getVariableValue("tenMoreThatFive")).isEqualTo(true);
  }

  @Test
  public void addTwoNumbers() {
    runSomeCode("morrigan says that a is 5. morrigan says that b is 5. morrigan says that c is a + b.");
    assertThat(morrigan.getInterpreter().getEnvironment().getVariableValue("c")).isEqualTo(10.0d);
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
