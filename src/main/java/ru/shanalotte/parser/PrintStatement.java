package ru.shanalotte.parser;

import lombok.Data;

@Data
public class PrintStatement extends Expression {
  private final Expression expression;
  @Override
  <R> R accept(Visitor<R> visitor) {
    return visitor.visit(this);
  }
}
