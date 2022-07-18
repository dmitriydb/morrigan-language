package ru.shanalotte.parser;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.shanalotte.scanner.Token;

@RequiredArgsConstructor
@ToString
@Data
public class UnaryExpression extends Expression{
  private final Token operator;
  private final Expression expression;

  @Override
  <R> R accept(Visitor<R> visitor) {
    return visitor.visit(this);
  }
}
