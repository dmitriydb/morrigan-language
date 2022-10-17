package ru.shanalotte.statements;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.shanalotte.expression.Expression;
import ru.shanalotte.parser.Visitor;

@RequiredArgsConstructor
@Getter
public class IfStatement extends Statement {
  private final Expression condition;
  private final Expression trueBranch;
  private final Expression falseBranch;

  @Override
  public <R> R accept(Visitor<R> visitor) {
    return visitor.visit(this);
  }
}
