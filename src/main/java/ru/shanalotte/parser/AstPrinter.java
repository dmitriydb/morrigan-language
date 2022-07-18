package ru.shanalotte.parser;

public class AstPrinter implements Visitor<String> {
  public String print(Expression expr) {
    return expr.accept(this);
  }

  @Override
  public String visit(BinaryExpression binaryExpression) {
    return parenthesize(binaryExpression.operator.getLexeme(),
        binaryExpression.getLeftSide(), binaryExpression.getRightSide());
  }

  @Override
  public String visit(Literal literal) {
    if (literal.getLiteral() == null) return "null";
    return literal.getLiteral().toString();
  }
  @Override
  public String visit(UnaryExpression unaryExpression) {
    return parenthesize(unaryExpression.getOperator().getLexeme(), unaryExpression.getExpression());
  }

  private String parenthesize(String name, Expression... exprs) {
    StringBuilder builder = new StringBuilder();
    builder.append("(").append(name);
    for (Expression expr : exprs) {
      builder.append(" ");
      builder.append(expr.accept(this));
    }
    builder.append(")");
    return builder.toString();
  }

}