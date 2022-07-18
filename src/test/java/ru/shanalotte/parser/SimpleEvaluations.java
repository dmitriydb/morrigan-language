package ru.shanalotte.parser;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class SimpleEvaluations {

  private Morrigan morrigan = new Morrigan();

  @Test
  public void evaluatesNumbers() {
    assertThat(morrigan.interpret("3")).isEqualTo(3);
  }

  @Test
  public void evaluatesNegativeNumber() {
    assertThat(morrigan.interpret("-3")).isEqualTo(-3);
  }

  @Test
  public void evaluatesString() {
    assertThat(morrigan.interpret("abc")).isEqualTo("abc");
  }

  @Test
  public void evaluatesTrue() {
    assertThat(morrigan.interpret("true")).isEqualTo(true);
  }

  @Test
  public void evaluatesFalse() {
    assertThat(morrigan.interpret("false")).isEqualTo(false);
  }

}