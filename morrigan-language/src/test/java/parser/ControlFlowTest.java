package parser;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class ControlFlowTest extends AbstractInterpreterTest{

  @Test
  public void ifStatementTest() {
    morrigan.interpret("morrigan says that if 3 > 2 then morrigan says that a is 3.");
    assertThat(morrigan.getInterpreter().getEnvironment().variableExists("a"));
    assertThat(morrigan.getInterpreter().getEnvironment().getVariableValue("a")).isEqualTo(3.0d);
  }

  @Test
  public void ifStatementElseCaseTest() {
    morrigan.interpret("morrigan says that if 5 > 10 then morrigan says that a is 10 else morrigan says that a is 5.");
    assertThat(morrigan.getInterpreter().getEnvironment().getVariableValue("a")).isEqualTo(5.0d);
  }

  @Test
  public void maxFunctionImplementation() {
    morrigan.interpret("morrigan says that a is 5."
        + "morrigan says that b is 10."
        + "morrigan says that if a > b then morrigan says that max is a else morrigan says that max is b.");
    assertThat(morrigan.getInterpreter().getEnvironment().getVariableValue("max")).isEqualTo(10.0d);
  }
}
