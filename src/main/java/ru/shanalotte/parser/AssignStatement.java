package ru.shanalotte.parser;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AssignStatement extends Expression{
  private final Variable variable;
  private final Expression expression;
  @Override
  <R> R accept(Visitor<R> visitor) {
    return visitor.visit(this);
  }
}
