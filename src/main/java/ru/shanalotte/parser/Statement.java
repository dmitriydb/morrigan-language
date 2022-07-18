package ru.shanalotte.parser;

public class Statement extends Expression{
  @Override
  <R> R accept(Visitor<R> visitor) {
    return this.accept(visitor);
  }
}
