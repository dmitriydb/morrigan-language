package ru.shanalotte.expression;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.shanalotte.parser.Visitor;
import ru.shanalotte.scanner.Token;

@Data
@RequiredArgsConstructor
public class BinaryExpression extends Expression {
  final Expression leftSide;
  final Token operator;
  final Expression rightSide;

  @Override
  public <R> R accept(Visitor<R> visitor) {
    return visitor.visit(this);
  }
}
