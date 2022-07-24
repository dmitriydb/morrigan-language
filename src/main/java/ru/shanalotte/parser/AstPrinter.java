package ru.shanalotte.parser;

import ru.shanalotte.expression.BinaryExpression;
import ru.shanalotte.expression.Expression;
import ru.shanalotte.expression.Literal;
import ru.shanalotte.expression.UnaryExpression;
import ru.shanalotte.statements.AssignStatement;
import ru.shanalotte.statements.IfStatement;
import ru.shanalotte.statements.PrintStatement;

public class AstPrinter implements Visitor<String> {
  public String print(Expression expr) {
    return expr.accept(this);
  }

  @Override
  public String visit(BinaryExpression binaryExpression) {
    return parenthesize(binaryExpression.getOperator().getLexeme(),
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

  @Override
  public String visit(PrintStatement printStatement) {
    return "PRINT " + printStatement.getExpression().accept(this);
  }

  @Override
  public String visit(AssignStatement assignStatement) {
    return assignStatement.getIdentifier().getLexeme() + " = " + assignStatement.getExpression().accept(this);

  }

  @Override
  public String visit(IfStatement ifStatement) {
    return null;
  }
}