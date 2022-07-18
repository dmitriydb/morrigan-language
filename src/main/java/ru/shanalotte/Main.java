package ru.shanalotte;

import java.io.File;
import java.io.IOException;
import java.util.List;
import ru.shanalotte.parser.Expression;
import ru.shanalotte.parser.Interpreter;
import ru.shanalotte.parser.Parser;
import ru.shanalotte.scanner.Scanner;
import ru.shanalotte.scanner.Token;

public class Main {

  public static void main(String[] args) throws IOException {
    Scanner scanner = new Scanner();
    List<Token> tokens = scanner.scan(new File("code.txt"));
    Expression root = new Parser(tokens).parse();
    Object value = new Interpreter().evaluate(root);
    System.out.println(value);
  }

}
