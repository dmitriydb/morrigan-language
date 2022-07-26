package ru.shanalotte;

import java.util.List;
import ru.shanalotte.expression.Expression;
import ru.shanalotte.interpreter.Interpreter;
import ru.shanalotte.parser.Parser;
import ru.shanalotte.scanner.Scanner;
import ru.shanalotte.scanner.Token;
import ru.shanalotte.statements.Statement;

public class Morrigan {

  public Object evaluate(String line) {
    Scanner scanner = new Scanner();
    List<Token> tokens = scanner.scan(line);
    Expression root = new Parser(tokens).parseExpression();
    return new Interpreter().evaluate(root);
  }


  public void interpret (String line) {
    Scanner scanner = new Scanner();
    List<Token> tokens = scanner.scan(line);
    List<Statement> statements = new Parser(tokens).parse();
    for (Statement st : statements) {
      new Interpreter().evaluate(st);
    }
  }
}
