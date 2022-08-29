package ru.shanalotte;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.shanalotte.expression.Expression;
import ru.shanalotte.interpreter.Interpreter;
import ru.shanalotte.parser.Parser;
import ru.shanalotte.scanner.Scanner;
import ru.shanalotte.scanner.Token;
import ru.shanalotte.statements.Statement;

@RequiredArgsConstructor
public class Morrigan {

  @Getter
  private final Interpreter interpreter;
  @Getter
  private final Scanner scanner;

  public Morrigan() {
    this.interpreter = new Interpreter();
    this.scanner = new Scanner();
  }

  public Object evaluate(String line) {
    interpreter.clearResults();
    List<Token> tokens = scanner.scan(line);
    Expression root = new Parser(tokens).parseExpression();
    return interpreter.evaluate(root);
  }

  public void interpret (String line) {
    interpreter.clearResults();
    List<Token> tokens = scanner.scan(line);
    List<Statement> statements = new Parser(tokens).parse();
    for (Statement st : statements) {
      interpreter.evaluate(st);
    }
  }
}
