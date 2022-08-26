package ru.shanalotte.parser;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import ru.shanalotte.Morrigan;
import ru.shanalotte.interpreter.Interpreter;
import ru.shanalotte.scanner.Scanner;

public class ReturningValuesTest {

  @Test
  public void interpreterShouldReturnResult() {
    Interpreter interpreter = new Interpreter();
    Scanner scanner = new Scanner();
    Morrigan morrigan = new Morrigan(interpreter, scanner);
    morrigan.interpret("morrigan says that a is [Hello world]. morrigan remembers what is a.");
    assertThat(morrigan.getInterpreter().getResult()).contains("Hello world");
  }

  @Test
  public void interpreterShouldClearResultBetweenInterprets() {
    Interpreter interpreter = new Interpreter();
    Scanner scanner = new Scanner();
    Morrigan morrigan = new Morrigan(interpreter, scanner);
    morrigan.interpret("morrigan says that a is [Hello world]. morrigan remembers what is a.");
    morrigan.interpret("morrigan says that a is [Hello world 2]. morrigan remembers what is a.");
    assertThat(morrigan.getInterpreter().getResult()).contains("Hello world 2");
    assertThat(morrigan.getInterpreter().getResult()).contains("Hello world");
  }

  @Test
  public void testingSomeProblem() {
    String code = "morrigan says that max is function(a, b) {morrigan says that if a > b then morrigan returns a else morrigan returns b}."
        + "morrigan remembers what is max(10, 5)."
        + "morrigan remembers what is max(100, 50)."
        + "morrigan remembers what is max(1, 117).";
    Interpreter interpreter = new Interpreter();
    Scanner scanner = new Scanner();
    Morrigan morrigan = new Morrigan(interpreter, scanner);
    morrigan.interpret(code);
    assertThat(interpreter.getResult()).containsExactlyInAnyOrder("100", "10", "117");
  }
}
