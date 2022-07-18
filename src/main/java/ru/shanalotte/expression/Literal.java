package ru.shanalotte.expression;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.shanalotte.expression.Expression;
import ru.shanalotte.parser.Visitor;

@RequiredArgsConstructor
@ToString
@Data
public class Literal extends Expression {
  private final Object literal;

  @Override
  public <R> R accept(Visitor<R> visitor) {
    return visitor.visit(this);
  }
}
