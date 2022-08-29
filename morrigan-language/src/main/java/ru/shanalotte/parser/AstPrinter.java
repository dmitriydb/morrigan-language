package ru.shanalotte.parser;

import ru.shanalotte.expression.BinaryExpression;
import ru.shanalotte.expression.CallExpression;
import ru.shanalotte.expression.Expression;
import ru.shanalotte.expression.Literal;
import ru.shanalotte.expression.LogicalExpression;
import ru.shanalotte.expression.UnaryExpression;
import ru.shanalotte.statements.AssignStatement;
import ru.shanalotte.statements.CallStatement;
import ru.shanalotte.statements.FunctionDeclarationStatement;
import ru.shanalotte.statements.IfStatement;
import ru.shanalotte.statements.PrintStatement;
import ru.shanalotte.statements.ReturnStatement;
import ru.shanalotte.statements.StatementGroup;
import ru.shanalotte.statements.WhileStatement;

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

  @Override
  public String visit(WhileStatement whileStatement) {
    return null;
  }

  @Override
  public String visit(StatementGroup statementGroup) {
    return null;
  }

  @Override
  public String visit(LogicalExpression logicalExpression) {
    return null;
  }

  @Override
  public String visit(CallExpression callExpression) {
    return null;
  }

  @Override
  public String visit(FunctionDeclarationStatement functionDeclarationStatement) {
    return null;
  }

  @Override
  public String visit(CallStatement callStatement) {
    return null;
  }

  @Override
  public String visit(ReturnStatement returnStatement) {
    return null;
  }
}