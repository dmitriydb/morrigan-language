package printer;

import java.util.List;
import org.junit.jupiter.api.Test;
import parser.AbstractInterpreterTest;
import ru.shanalotte.interpreter.Interpreter;
import ru.shanalotte.parser.AstPrinter;
import ru.shanalotte.parser.Parser;
import ru.shanalotte.scanner.Scanner;
import ru.shanalotte.scanner.Token;
import ru.shanalotte.statements.Statement;

public class AstPrinterTest extends AbstractInterpreterTest {

  @Test
  public void case1() {
    print("morrigan remembers what is 1 + 1 = 2 + 1.");
  }

  @Test
  public void case2() {
    print("morrigan says that a is 5."
        + "morrigan says that b is 10."
        + "morrigan says that if a > b then morrigan says that max is a else morrigan says that max is b.");
  }

  @Test
  public void case3() {
    print("morrigan says that if 3 > 2 then morrigan says that a is 3.");
  }

  @Test
  public void case4() {
    print("morrigan remembers what is 2 < 3 = 3 < 4.");
  }

  @Test
  public void case5() {
    print("morrigan remembers what is concat(1, 2).");
  }

  @Test
  public void case6() {
    print("morrigan says that helloWorld is function() {"
        + "morrigan remembers what is [Hello, World]}.");
  }

  @Test
  public void case7() {
    print("morrigan says that helloWorld is function() {"
        + "morrigan says that a is 5}. morrigan remembers what is helloWorld().");
  }

  @Test
  public void case8() {
    print("morrigan says that a is 2 * 2 * 2 * 3 + 1 / 2. "
        + "morrigan says that test is function() {"
        + "morrigan says that a is 5 and morrigan remembers what is a"
        + "}."
        + "morrigan remembers what is test()."
        + "morrigan remembers what is a.");
  }

  @Test
  public void case9() {
    print("morrigan says that a is function() {morrigan returns 2 * 2 + 3 / 2}."
        + "morrigan says that b is a().");
  }

  @Test
  public void case10() {
    print("morrigan says that a is 10."
        + "morrigan says that b is 15."
        + "morrigan says that max is function(num1, num2) { "
        + "  morrigan says that if num1 > num2 then morrigan returns num1 else"
        + "  morrigan returns num2"
        + "}."
        + "morrigan says that result is max(a, b)."
        + "morrigan remembers what is result.");
  }

  @Test
  public void case11() {
    print("morrigan says that n is 5."
        + "morrigan says that factorial is function(n) { "
        + "morrigan says that if n = 1 then morrigan returns 1 else morrigan returns n * factorial(n - 1)}."
        + "morrigan says that a is factorial(n). morrigan remembers what is a.");
  }

  @Test
  public void case12() {
    print("morrigan says that i is 1, sum is 0. morrigan says that while i > 0 & i < 5 morrigan says that sum is sum + 1 and morrigan says that i is i + 1.");
  }

  @Test
  public void case13() {
    print("morrigan says that if 2 > 3 | 3 > 2 then morrigan says that a is passed.");
  }

  @Test
  public void case14() {
    print("morrigan says that a is 0."
        + "morrigan says that a is a + 1 x 10.");
  }

  @Test
  public void case15() {
    String code = "morrigan says that max is function(a, b) {morrigan says that if a > b then morrigan returns a else morrigan returns b}."
        + "morrigan remembers what is max(10, 5)."
        + "morrigan remembers what is max(100, 50)."
        + "morrigan remembers what is max(1, 117).";
    print(code);
  }

  @Test
  public void case16() {
    print("morrigan says that if 10 > 5 then "
        + "  morrigan says that a is 1 and morrigan says that b is 1."
        + "morrigan says that if 10 < 5 then "
        + "  morrigan says that c is 2 and morrigan says that d is 2 "
        + "else"
        + "  morrigan says that c is 3 and morrigan says that d is 3 + 3.");
  }

  @Test
  public void case17() {
    print("morrigan says that a is 10, b is 5. morrigan says that if a > b then morrigan remembers what is [a > b] and morrigan remembers what is [more] else   morrigan remembers what is [a < b] and morrigan remembers what is [less].");
  }

  @Test
  public void case18() {
    print("morrigan says that firstCentsAmount is 11. morrigan says that secondCentsAmount is 22. morrigan says that totalCents is firstCentsAmount + secondCentsAmount. morrigan remembers what is 0 + o + totalCents.");
  }

  private void print(String code) {
    List<Token> tokens = new Scanner().scan(code);
    List<Statement> statements = new Parser(tokens).parse();
    AstPrinter astPrinter = new AstPrinter();
    for (var statement : statements) {
      astPrinter.print(statement);
    }
  }
}
