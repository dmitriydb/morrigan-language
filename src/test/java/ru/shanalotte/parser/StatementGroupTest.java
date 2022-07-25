package ru.shanalotte.parser;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import ru.shanalotte.environment.Environment;

public class StatementGroupTest extends AbstractInterpreterTest{

  @Test
  public void simpleStatementGroupTest() {
    morrigan.interpret("morrigan says that a is 3, b is 5.");
    assertThat(Environment.getGlobalVariableValue("a")).isEqualTo(3);
    assertThat(Environment.getGlobalVariableValue("b")).isEqualTo(5);
  }

  @Test
  public void ifStatementGroupTest() {
    morrigan.interpret("morrigan says that if 10 > 5 then "
        + "  morrigan says that a is 1 and morrigan says that b is 1."
        + "morrigan says that if 10 < 5 then "
        + "  morrigan says that c is 2 and morrigan says that d is 2 "
        + "else"
        + "  morrigan says that c is 3 and morrigan says that d is 3.");
    assertThat(Environment.getGlobalVariableValue("a")).isEqualTo(1);
    assertThat(Environment.getGlobalVariableValue("b")).isEqualTo(1);
    assertThat(Environment.getGlobalVariableValue("c")).isEqualTo(3);
    assertThat(Environment.getGlobalVariableValue("d")).isEqualTo(3);
  }

  @Test
  public void comparisonTest() {
    morrigan.interpret("morrigan says that a is 10, b is 5. morrigan says that if a > b then morrigan remembers what is [a > b] and morrigan remembers what is [more] else   morrigan remembers what is [a < b] and morrigan remembers what is [less].");
  }

  @Test
  public void comparisonTest2() {
    morrigan.interpret("morrigan says that a is 10, b is 5. morrigan says that if a < b then morrigan remembers what is [a > b] and morrigan remembers what is [more] else morrigan remembers what is [a < b] and morrigan remembers what is [less].");
  }

  @Test
  public void whileWithStatementGroupTest() {
    morrigan.interpret("morrigan says that counter is 0. morrigan says that while counter < 11 morrigan remembers what is [now a is ] + counter and morrigan says that counter is counter + 1."
        );
  }
}
