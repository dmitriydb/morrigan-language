package parser;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class BinaryOperationsTest extends AbstractInterpreterTest{

  @Test
  public void addingNumbers() {
    assertThat(morrigan.evaluate("2  + 3")).isEqualTo(5.0d);
  }

  @Test
  public void addingNumberAndFalse() {
    assertThat(morrigan.evaluate("2   + false")).isEqualTo("2false");
  }

  @Test
  public void addingNumberAndTrue() {
    assertThat(morrigan.evaluate("2 + true")).isEqualTo("2true");
  }

  @Test
  public void addingTrueAndNumber() {
    assertThat(morrigan.evaluate("true + 666")).isEqualTo("true666");
  }

  @Test
  public void addingNumberAndString() {
    assertThat(morrigan.evaluate("2   + abc")).isEqualTo("2abc");
  }

  @Test
  public void addingStringAndNumber() {
    assertThat(morrigan.evaluate("xka + 23")).isEqualTo("xka23");
  }

  @Test
  public void addingNegativeNumbers() {
    assertThat(morrigan.evaluate("-3 + -3")).isEqualTo(-6.0d);
  }

  @Test
  public void multiplyingNumbers() {
    assertThat(morrigan.evaluate("-3 * -3")).isEqualTo(9.0d);
  }

  @Test
  public void multiplyingStringAndNumberIsIllegal() {
    assertThrows(Throwable.class, () -> {
      morrigan.evaluate("3 * abc");
    });
  }

  @Test
  public void multiplyingBooleanAndNumberIsIllegal() {
    assertThrows(Throwable.class, () -> {
      morrigan.evaluate("3 * true");
    });
  }

  @Test
  public void multiplyingBooleansIsIllegal() {
    assertThrows(Throwable.class, () -> {
      morrigan.evaluate("false * true");
    });
  }

  @Test
  public void multiplyingStringsIsIllegal() {
    assertThrows(Throwable.class, () -> {
      morrigan.evaluate("a * c");
    });
  }

  @Test
  public void comparingInts() {
    assertThat(morrigan.evaluate("3 > -2")).isEqualTo(true);
    assertThat(morrigan.evaluate("3 < -2")).isEqualTo(false);
    assertThat(morrigan.evaluate("0 = 0")).isEqualTo(true);
    assertThat(morrigan.evaluate("-555 = -555")).isEqualTo(true);
  }

  @Test
  public void comparingStrings() {
    assertThat(morrigan.evaluate("xxx > yyy")).isEqualTo(false);
    assertThat(morrigan.evaluate("9 > ab")).isEqualTo(false);
    assertThat(morrigan.evaluate("a1b1 = a1b1")).isEqualTo(true);
  }

  @Test
  public void comparingBooleans() {
    assertThat(morrigan.evaluate("1 + 1 = 1 + 1")).isEqualTo(true);
    assertThat(morrigan.evaluate("1 + 1 = 2 + 1")).isEqualTo(false);
    assertThat(morrigan.evaluate("2 > 1 = 2 > 1")).isEqualTo(true);
    assertThat(morrigan.evaluate("2 > 1 = 2 > 2")).isEqualTo(false);
  }

  @Test
  public void illegalComparings() {
    assertThrows(Throwable.class, () -> {
      morrigan.evaluate("true > false");
    });
  }

}