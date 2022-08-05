package ru.shanalotte;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;
import ru.shanalotte.interpreter.Interpreter;

public class Main {

  public static void main(String[] args) throws IOException {
    Scanner scanner = new Scanner(System.in);
    String nextLine = "";
    do {
      nextLine = scanner.nextLine();
      if (Objects.equals(nextLine, "quit")) {
        break;
      }
      try {
        new Morrigan(new Interpreter(), new ru.shanalotte.scanner.Scanner()).interpret(nextLine);
      } catch (Throwable t) {
        System.out.println(t.getMessage());
      }
    } while (!nextLine.equals("quit"));
    System.out.println("morrigan goes to sleep.");
  }

}
