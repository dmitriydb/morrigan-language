package ru.shanalotte.parser;

import org.junit.jupiter.api.BeforeEach;
import ru.shanalotte.Morrigan;
import ru.shanalotte.environment.Environment;

public class AbstractInterpreterTest {

  protected Morrigan morrigan = new Morrigan();

  @BeforeEach
  public void clearEnvironment() {
    Environment.clear();
  }

}
