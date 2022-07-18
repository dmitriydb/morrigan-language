package ru.shanalotte.parser;

public interface Visitor<R> {
  R visit(BinaryExpression binaryExpression);
  R visit(Literal literal);
  R visit(UnaryExpression unaryExpression);
  R visit(Statement statement);
  R visit(PrintStatement printStatement);
  R visit(AssignStatement assignStatement);

}
