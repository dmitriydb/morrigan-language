package ru.shanalotte.parser;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class UnaryOperationsTest {

  private Morrigan morrigan = new Morrigan();

  @Test
  public void minusNumber() {
    assertThat(morrigan.interpret("-3")).isEqualTo(-3);
  }

  @Test
  public void plusNumber() {
    assertThat(morrigan.interpret("+3")).isEqualTo(3);
  }

  @Test
  public void plus3254() {
    assertThat(morrigan.interpret("+3254")).isEqualTo(3254);
  }

  @Test
  public void plusString() {
    assertThrows(Throwable.class, () -> {
      morrigan.interpret("+abc");
    });
  }

  @Test
  public void minusString() {
    assertThrows(Throwable.class, () -> {
      morrigan.interpret("-abc");
    });
  }

  @Test
  public void unaryStarIsNotAllowed() {
    assertThrows(Throwable.class, () -> {
      System.out.println(morrigan.interpret("*3"));
    });
  }

  @Test
  public void unarySlashIsNotAllowed() {
    assertThrows(Throwable.class, () -> {
      System.out.println(morrigan.interpret("/3"));
    });
  }

  @Test
  public void plusOnBoolean() {
    assertThrows(Throwable.class, () -> {
      System.out.println(morrigan.interpret("+false"));
    });
  }

  @Test
  public void minusOnBoolean() {
    assertThrows(Throwable.class, () -> {
      System.out.println(morrigan.interpret("-false"));
    });
  }

}