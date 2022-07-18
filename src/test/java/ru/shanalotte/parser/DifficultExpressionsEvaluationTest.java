package ru.shanalotte.parser;


import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class DifficultExpressionsEvaluationTest {

  private Morrigan morrigan = new Morrigan();

  @Test
  public void comparing2and3and4() {
    assertThat(morrigan.interpret("2 < 3 = 3 < 4")).isEqualTo(true);
  }
}
