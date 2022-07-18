package ru.shanalotte.parser;

public abstract class Expression {
  abstract <R> R accept (Visitor<R> visitor);
}
