package ru.shanalotte.parser;

import java.util.List;
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

  public void print(Expression expr) {
    System.out.println(expr.accept(this));
  }

  @Override
  public String visit(BinaryExpression binaryExpression) {
    return parenthesize(binaryExpression.getOperator().getLexeme(),
        binaryExpression.getLeftSide(), binaryExpression.getRightSide());
  }

  @Override
  public String visit(Literal literal) {
    if (literal.getLiteral() == null) {
      return "null";
    }
    return literal.getLiteral().toString();
  }

  @Override
  public String visit(UnaryExpression unaryExpression) {
    return parenthesize(unaryExpression.getOperator().getLexeme(), unaryExpression.getExpression());
  }

  @Override
  public String visit(PrintStatement printStatement) {
    return "PRINT " + printStatement.getExpression().accept(this);
  }

  @Override
  public String visit(AssignStatement assignStatement) {
    return assignStatement.getIdentifier().getLexeme() + " := "
        + assignStatement.getExpression().accept(this);
  }

  @Override
  public String visit(IfStatement ifStatement) {
    if (ifStatement.getFalseBranch() == null) {
      return parenthesize("THEN " + ifStatement.getTrueBranch().accept(this) + " "
          + parenthesize("IF", ifStatement.getCondition()));
    } else {
      return parenthesize("ELSE " + ifStatement.getFalseBranch().accept(this) + " "
          + parenthesize("THEN " + ifStatement.getTrueBranch().accept(this) + " "
          + parenthesize("IF", ifStatement.getCondition())));
    }
  }

  @Override
  public String visit(WhileStatement whileStatement) {
    return "DO " + whileStatement.getLoopStatement().accept(this)
        + " WHILE " + whileStatement.getLoopCondition().accept(this);
  }

  @Override
  public String visit(StatementGroup statementGroup) {
    StringBuilder result = new StringBuilder();
    for (var statement : statementGroup.getStatements()) {
      result.append(statement.accept(this)).append(", ");
    }
    if (!result.isEmpty()) {
      result.deleteCharAt(result.length() - 1);
      result.deleteCharAt(result.length() - 1);
    }
    return result.toString();
  }

  @Override
  public String visit(LogicalExpression logicalExpression) {
    StringBuilder result = new StringBuilder();
    var operand = logicalExpression.getOperands().iterator();
    var operator = logicalExpression.getOperators().iterator();
    var first = operand.next();
    while (operator.hasNext()) {
      var nextOperator = operator.next();
      var second = operand.next();
      String localResult = parenthesize(nextOperator.name(), first, second);
      first = second;
      result.append(localResult).append(" ");
    }
    return result.toString();
  }

  @Override
  public String visit(CallExpression callExpression) {
    var result = new StringBuilder();
    result.append(callExpression.getCallee().accept(this)).append(" ");
    for (var arg : callExpression.getArguments()) {
      result.append(arg.accept(this)).append(" ");
    }
    if (!result.isEmpty()) {
      result.deleteCharAt(result.length() - 1);
    }
    return parenthesize("CALL " + result);
  }

  @Override
  public String visit(FunctionDeclarationStatement functionDeclarationStatement) {
    return "FUN " + functionDeclarationStatement.getFunctionName() + " ::= "
        + functionDeclarationStatement.getFunctionBody().accept(this);
  }

  @Override
  public String visit(CallStatement callStatement) {
    return callStatement.getFunctionCall().accept(this);
  }

  @Override
  public String visit(ReturnStatement returnStatement) {
    return parenthesize("RETURN", returnStatement.getReturnExpression());
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

  private String parenthesize(String name, List<Expression> exprs) {
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