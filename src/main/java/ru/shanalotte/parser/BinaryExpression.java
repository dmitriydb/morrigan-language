package ru.shanalotte.parser;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.shanalotte.scanner.Token;

import ru.shanalotte.scanner.Token;

@Data
@RequiredArgsConstructor
public class BinaryExpression extends Expression{
  final Expression leftSide;
  final Token operator;
  final Expression rightSide;

  @Override
  <R> R accept(Visitor<R> visitor) {
    return visitor.visit(this);
  }
}
