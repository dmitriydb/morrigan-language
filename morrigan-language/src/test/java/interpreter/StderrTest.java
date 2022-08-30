package interpreter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import ru.shanalotte.Morrigan;
import ru.shanalotte.interpreter.Interpreter;
import ru.shanalotte.scanner.Scanner;

public class StderrTest {

  @Test
  public void shouldWriteToStdErr() {
    Morrigan morrigan = new Morrigan(new Interpreter(), new Scanner());
    assertThrows(Throwable.class, () -> {
      morrigan.interpret(".");
    });
    assertThat(morrigan.getStderr()).contains("what?");
  }

  @Test
  public void writingSomethingToStdErr() {
    Morrigan morrigan = new Morrigan(new Interpreter(), new Scanner());
    assertThrows(Throwable.class, () -> {
      morrigan.interpret("morrigan says that a = 3.");
    });
    System.out.println(morrigan.getStderr());
  }

  @Test
  public void shouldClearStdErrBetweenCals() {
    Morrigan morrigan = new Morrigan(new Interpreter(), new Scanner());
    assertThrows(Throwable.class, () -> {
      morrigan.interpret(".");
    });
    morrigan.interpret("morrigan says that a is 3.");
    assertThat(morrigan.getStderr()).isEmpty();
  }
}
