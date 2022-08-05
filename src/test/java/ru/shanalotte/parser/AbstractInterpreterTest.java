package ru.shanalotte.parser;

import org.junit.jupiter.api.BeforeEach;
import ru.shanalotte.Morrigan;
import ru.shanalotte.environment.Environment;
import ru.shanalotte.interpreter.Interpreter;
import ru.shanalotte.scanner.Scanner;

public class AbstractInterpreterTest {

  protected Morrigan morrigan = new Morrigan(new Interpreter(), new Scanner());

  @BeforeEach
  public void refreshMorrigan() {
    morrigan = new Morrigan(new Interpreter(), new Scanner());
  }
}
