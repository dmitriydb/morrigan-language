package ru.shanalotte.parser;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class BinaryOperationsTest {

  private Morrigan morrigan = new Morrigan();

  @Test
  public void addingNumbers() {
    assertThat(morrigan.interpret("2   + 3")).isEqualTo(5);
  }

  @Test
  public void addingNumberAndFalse() {
    assertThat(morrigan.interpret("2   + false")).isEqualTo("2false");
  }

  @Test
  public void addingNumberAndTrue() {
    assertThat(morrigan.interpret("2   + true")).isEqualTo("2true");
  }

  @Test
  public void addingTrueAndNumber() {
    assertThat(morrigan.interpret("true + 666")).isEqualTo("true666");
  }

  @Test
  public void addingNumberAndString() {
    assertThat(morrigan.interpret("2   + abc")).isEqualTo("2abc");
  }

  @Test
  public void addingStringAndNumber() {
    assertThat(morrigan.interpret("xka + 23")).isEqualTo("xka23");
  }

  @Test
  public void addingNegativeNumbers() {
    assertThat(morrigan.interpret("-3 + -3")).isEqualTo(-6);
  }

  @Test
  public void multiplyingNumbers() {
    assertThat(morrigan.interpret("-3 * -3")).isEqualTo(9);
  }

  @Test
  public void multiplyingStringAndNumberIsIllegal() {
    assertThrows(Throwable.class, () -> {
      morrigan.interpret("3 * abc");
    });
  }

  @Test
  public void multiplyingBooleanAndNumberIsIllegal() {
    assertThrows(Throwable.class, () -> {
      morrigan.interpret("3 * true");
    });
  }

  @Test
  public void multiplyingBooleansIsIllegal() {
    assertThrows(Throwable.class, () -> {
      morrigan.interpret("false * true");
    });
  }

  @Test
  public void multiplyingStringsIsIllegal() {
    assertThrows(Throwable.class, () -> {
      morrigan.interpret("a * c");
    });
  }

  @Test
  public void comparingInts() {
    assertThat(morrigan.interpret("3 > -2")).isEqualTo(true);
    assertThat(morrigan.interpret("3 < -2")).isEqualTo(false);
    assertThat(morrigan.interpret("0 = 0")).isEqualTo(true);
    assertThat(morrigan.interpret("-555 = -555")).isEqualTo(true);
  }

  @Test
  public void comparingStrings() {
    assertThat(morrigan.interpret("xxx > yyy")).isEqualTo(false);
    assertThat(morrigan.interpret("9 > ab")).isEqualTo(false);
    assertThat(morrigan.interpret("a1b1 = a1b1")).isEqualTo(true);
  }

  @Test
  public void comparingBooleans() {
    assertThat(morrigan.interpret("1 + 1 = 1 + 1")).isEqualTo(true);
    assertThat(morrigan.interpret("1 + 1 = 2 + 1")).isEqualTo(false);
    assertThat(morrigan.interpret("2 > 1 = 2 > 1")).isEqualTo(true);
    assertThat(morrigan.interpret("2 > 1 = 2 > 2")).isEqualTo(false);
  }

  @Test
  public void illegalComparings() {
    assertThrows(Throwable.class, () -> {
      morrigan.interpret("true > false");
    });
  }

}