package ru.shanalotte.parser;

import java.util.List;
import ru.shanalotte.scanner.Scanner;
import ru.shanalotte.scanner.Token;

public class Morrigan {

  public Object interpret(String line) {
    Scanner scanner = new Scanner();
    List<Token> tokens = scanner.scan(line);
    Expression root = new Parser(tokens).parse();
    return new Interpreter().evaluate(root);
  }
}
