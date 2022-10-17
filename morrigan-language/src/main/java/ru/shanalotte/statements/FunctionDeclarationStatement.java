package ru.shanalotte.statements;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.shanalotte.parser.Visitor;
import ru.shanalotte.scanner.Token;

@RequiredArgsConstructor
@Getter
public class FunctionDeclarationStatement extends Statement {

  private final String functionName;
  private final List<Token> parameters;
  private final Statement functionBody;

  @Override
  public <R> R accept(Visitor<R> visitor) {
    return visitor.visit(this);
  }
}
