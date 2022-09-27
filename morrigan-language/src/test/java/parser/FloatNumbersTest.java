package parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withPrecision;
import org.junit.jupiter.api.Test;
import ru.shanalotte.environment.Environment;

public class FloatNumbersTest extends AbstractInterpreterTest {

  @Test
  public void should_declareFloatNumbers() {
    morrigan.interpret("morrigan says that a is 0,33.");
    assertThat(morrigan.getInterpreter().getEnvironment().getVariableValue("a")).isEqualTo(0.33d);
  }

  @Test
  public void floatNumberAdding() {
    morrigan.interpret("morrigan says that a is 0,1. morrigan says that b is 0,2. morrigan says that c is a + b.");
    double expectedValue = (double) morrigan.getInterpreter().getEnvironment().getVariableValue("c");
    assertThat(expectedValue).isEqualTo(0.3d, withPrecision(0.000001d));
  }

  @Test
  public void multipleDeclaring() {
    morrigan.interpret("morrigan says that a is 0,3, b is 0,5.");
    double expectedValueA = (double) morrigan.getInterpreter().getEnvironment().getVariableValue("a");
    double expectedValueB = (double) morrigan.getInterpreter().getEnvironment().getVariableValue("b");
    assertThat(expectedValueA).isEqualTo(0.3d, withPrecision(0.000001d));
    assertThat(expectedValueB).isEqualTo(0.5d, withPrecision(0.000001d));
  }

  @Test
  public void literalsTest() {
    morrigan.interpret("morrigan remembers what is 0,3 + 0,3.");
  }

}
