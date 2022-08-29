package parser;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import ru.shanalotte.scanner.Scanner;
import ru.shanalotte.scanner.Token;
import ru.shanalotte.scanner.TokenType;

public class StringLiteralTest extends AbstractInterpreterTest{

  @Test
  public void shouldTokenizeTextLiteral() {
    Scanner scanner = new Scanner();
    List<Token> tokens = scanner.scan("morrigan says that helloworld is [hello world!].");
    assertThat(tokens).anyMatch(token -> token.getTokenType() == TokenType.STRING);
  }

  @Test
  public void printingHelloWorldLiteral() {
    morrigan.interpret("morrigan says that helloWorld is [Hello, world!]."
        + "morrigan remembers what is helloWorld x 10.");
    assertThat(morrigan.getInterpreter().getEnvironment().getVariableValue("helloWorld")).isEqualTo("Hello, world!");
  }
}
