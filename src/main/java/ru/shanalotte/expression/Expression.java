package ru.shanalotte.expression;

import ru.shanalotte.parser.Visitor;

public abstract class Expression {
  public abstract <R> R accept(Visitor<R> visitor);
}
