package ru.shanalotte.parser;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
@Data
public class Literal extends Expression{
  private final Object literal;

  @Override
  <R> R accept(Visitor<R> visitor) {
    return visitor.visit(this);
  }
}
