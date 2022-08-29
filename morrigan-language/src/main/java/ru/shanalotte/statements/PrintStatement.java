package ru.shanalotte.statements;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.shanalotte.expression.Expression;
import ru.shanalotte.parser.Visitor;

@Data
@RequiredArgsConstructor
public class PrintStatement extends Statement {
  private final Expression expression;
  @Override
  public <R> R accept(Visitor<R> visitor) {
    return visitor.visit(this);
  }
}
