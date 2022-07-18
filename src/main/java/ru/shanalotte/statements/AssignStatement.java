package ru.shanalotte.statements;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.shanalotte.expression.Expression;
import ru.shanalotte.parser.Visitor;
import ru.shanalotte.scanner.Token;
import ru.shanalotte.statements.Statement;

@Data
@RequiredArgsConstructor
public class AssignStatement extends Statement {
  private final Token identifier;
  private final Expression expression;
  @Override
  public <R> R accept(Visitor<R> visitor) {
    return visitor.visit(this);
  }
}
