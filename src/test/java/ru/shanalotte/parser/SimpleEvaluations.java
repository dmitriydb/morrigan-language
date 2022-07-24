package ru.shanalotte.parser;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;
import ru.shanalotte.Morrigan;

public class SimpleEvaluations extends AbstractInterpreterTest{

  @Test
  public void evaluatesNumbers() {
    assertThat(morrigan.evaluate("3")).isEqualTo(3);
  }

  @Test
  public void evaluatesNegativeNumber() {
    assertThat(morrigan.evaluate("-3")).isEqualTo(-3);
  }

  @Test
  public void evaluatesString() {
    assertThat(morrigan.evaluate("abc")).isEqualTo("abc");
  }

  @Test
  public void evaluatesTrue() {
    assertThat(morrigan.evaluate("true")).isEqualTo(true);
  }

  @Test
  public void evaluatesFalse() {
    assertThat(morrigan.evaluate("false")).isEqualTo(false);
  }

}