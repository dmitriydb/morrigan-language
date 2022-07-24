package ru.shanalotte.parser;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import ru.shanalotte.Morrigan;

public class UnaryOperationsTest extends AbstractInterpreterTest{

  @Test
  public void minusNumber() {
    assertThat(morrigan.evaluate("-3")).isEqualTo(-3);
  }

  @Test
  public void plusNumber() {
    assertThat(morrigan.evaluate("+3")).isEqualTo(3);
  }

  @Test
  public void plus3254() {
    assertThat(morrigan.evaluate("+3254")).isEqualTo(3254);
  }

  @Test
  public void plusString() {
    assertThrows(Throwable.class, () -> {
      morrigan.evaluate("+abc");
    });
  }

  @Test
  public void minusString() {
    assertThrows(Throwable.class, () -> {
      morrigan.evaluate("-abc");
    });
  }

  @Test
  public void unaryStarIsNotAllowed() {
    assertThrows(Throwable.class, () -> {
      System.out.println(morrigan.evaluate("*3"));
    });
  }

  @Test
  public void unarySlashIsNotAllowed() {
    assertThrows(Throwable.class, () -> {
      System.out.println(morrigan.evaluate("/3"));
    });
  }

  @Test
  public void plusOnBoolean() {
    assertThrows(Throwable.class, () -> {
      System.out.println(morrigan.evaluate("+false"));
    });
  }

  @Test
  public void minusOnBoolean() {
    assertThrows(Throwable.class, () -> {
      System.out.println(morrigan.evaluate("-false"));
    });
  }

}