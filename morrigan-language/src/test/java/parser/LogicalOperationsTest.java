package parser;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class LogicalOperationsTest extends AbstractInterpreterTest {

  @Test
  public void shouldEvaluateLogicalOr() {
    morrigan.interpret("morrigan says that if 2 > 3 | 3 > 2 then morrigan says that a is passed.");
    assertThat(morrigan.getInterpreter().getEnvironment().getVariableValue("a")).isEqualTo("passed");
  }

  @Test
  public void shouldEvaluateLogicalAnd() {
    morrigan.interpret("morrigan says that if 2 > 3 & 3 > 2 then morrigan says that a is passed.");
    assertThat(morrigan.getInterpreter().getEnvironment().getVariableValue("a")).isNotEqualTo("passed");
  }

  @Test
  public void shouldEvaluateLogicalOperationInIf() {
    morrigan.interpret("morrigan says that i is 1, sum is 0. morrigan says that while i > 0 & i < 5 morrigan says that sum is sum + 1 and morrigan says that i is i + 1.");
    assertThat(morrigan.getInterpreter().getEnvironment().getVariableValue("sum")).isEqualTo(4.0d);
  }

}
