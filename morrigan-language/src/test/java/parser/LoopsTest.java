package parser;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class LoopsTest extends AbstractInterpreterTest{

  @Test
  public void shouldIncrementVariableTenTimes() {
    morrigan.interpret("morrigan says that a is 0."
        + "morrigan says that a is a + 1 x 10.");
    assertThat(morrigan.getInterpreter().getEnvironment().getVariableValue("a")).isEqualTo(10.0d);
  }

  @Test
  public void shouldPrintTenTimes() {
    morrigan.interpret("morrigan says that a is helloworld. morrigan remembers what is a x 10.");
  }

  @Test
  public void whileEvaluationTest() {
    morrigan.interpret("morrigan says that a is 0. "
        + "morrigan says that while a < 10 morrigan says that a is a + 1.");
    assertThat(morrigan.getInterpreter().getEnvironment().getVariableValue("a")).isEqualTo(10.0d);
  }

  @Test
  public void tenIsNotLessThenTen() {
    morrigan.interpret("morrigan says that a is 10 < 10.");
    assertThat(morrigan.getInterpreter().getEnvironment().getVariableValue("a")).isEqualTo(false);
  }

  @Test
  public void tenIsNotMoreThenTen() {
    morrigan.interpret("morrigan says that a is 10 > 10.");
    assertThat(morrigan.getInterpreter().getEnvironment().getVariableValue("a")).isEqualTo(false);
  }

  @Test
  public void tenEqualsTen() {
    morrigan.interpret("morrigan says that a is 10 = 10.");
    assertThat(morrigan.getInterpreter().getEnvironment().getVariableValue("a")).isEqualTo(true);
  }
}
