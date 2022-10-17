package ru.shanalotte.statements;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.shanalotte.expression.Expression;
import ru.shanalotte.parser.Visitor;

@RequiredArgsConstructor
public class CallStatement extends Statement {

  @Getter
  private final Expression functionCall;

  @Override
  public <R> R accept(Visitor<R> visitor) {
    return visitor.visit(this);
  }
}
