package ru.shanalotte.interpreter;

import ru.shanalotte.environment.Environment;
import ru.shanalotte.statements.AssignStatement;
import ru.shanalotte.expression.BinaryExpression;
import ru.shanalotte.expression.Expression;
import ru.shanalotte.expression.Literal;
import ru.shanalotte.statements.IfStatement;
import ru.shanalotte.statements.PrintStatement;
import ru.shanalotte.expression.UnaryExpression;
import ru.shanalotte.parser.Visitor;
import ru.shanalotte.scanner.TokenType;
import ru.shanalotte.statements.Statement;
import ru.shanalotte.statements.StatementGroup;
import ru.shanalotte.statements.WhileStatement;

public class Interpreter implements Visitor<Object> {
  @Override
  public Object visit(BinaryExpression binaryExpression) {
    Object leftValue = binaryExpression.getLeftSide().accept(this);
    Object rightValue = binaryExpression.getRightSide().accept(this);
    switch (binaryExpression.getOperator().getTokenType()) {
      case PLUS:
        if (isInteger(leftValue) && isInteger((rightValue))) {
          return (int) leftValue + (int) rightValue;
        } else {
          return String.valueOf(leftValue) + String.valueOf(rightValue);
        }
      case MINUS:
        if (bothNotIntegers(leftValue, rightValue)) {
          throw new IllegalArgumentException("Substracting strings/booleans is illegal: " + leftValue.toString() + ", " + rightValue.toString());
        }
        return (int) leftValue - (int) rightValue;
      case STAR:
        if (bothNotIntegers(leftValue, rightValue)) {
          throw new IllegalArgumentException("Multiplying strings/booleans is illegal: " + leftValue.toString() + ", " + rightValue.toString());
        }
        return (int) leftValue * (int) rightValue;
      case SLASH:
        if (bothNotIntegers(leftValue, rightValue)) {
          throw new IllegalArgumentException("Dividing strings/booleans is illegal: " + leftValue.toString() + ", " + rightValue.toString());
        }
        return (int) leftValue / (int) rightValue;
      case MORE:
        return isMore(leftValue, rightValue);
      case LESS:
        return !isMore(leftValue, rightValue) && !isEquals(leftValue, rightValue);
      case EQUALS:
        return isEquals(leftValue, rightValue);
      default:
        return 0;
    }
  }

  private boolean isMore(Object a, Object b) {
    if (isBoolean(a) && isBoolean(b)){
      throw new IllegalArgumentException("It is illegal to compare two boolean values. Were: " + a + ", " + b);
    }
    if (!bothNotIntegers(a, b)) {
      return (int) a > (int) b;
    }
    return String.valueOf(a).compareTo(String.valueOf(b)) > 0;
  }

  private boolean isEquals(Object a, Object b) {
    if (isBoolean(a) && isBoolean(b)){
      return  (a.equals(b));
    }
    return !isMore(a, b) && !isMore(b, a);
  }

  private boolean bothNotIntegers(Object leftValue, Object rightValue) {
    return !isInteger(leftValue) || !isInteger(rightValue);
  }

  @Override
  public Object visit(Literal literal) {
    if (literal.getLiteral().equals("true") || literal.getLiteral().equals("false")) {
      return Boolean.valueOf(literal.getLiteral().toString());
    }
    if (Environment.globalVariableExists(literal.getLiteral().toString())) {
      return Environment.getGlobalVariableValue(literal.getLiteral().toString());
    }
    try {
      int intValue = Integer.parseInt(literal.getLiteral().toString());
      return intValue;
    } catch (Throwable t) {
      return literal.getLiteral().toString();
    }
  }

  @Override
  public Object visit(IfStatement ifStatement) {
    boolean conditionResult = (boolean)ifStatement.getCondition().accept(this);
    if (conditionResult) {
      return ifStatement.getTrueBranch().accept(this);
    } else {
      return ifStatement.getFalseBranch().accept(this);
    }
  }

  @Override
  public Object visit(WhileStatement whileStatement) {
    while (evaluateCondition(whileStatement.getLoopCondition())) {
      evaluate(whileStatement.getLoopStatement());
    }
    return null;
  }

  private boolean evaluateCondition(Expression loopCondition) {
    return (boolean) loopCondition.accept(this);
  }

  @Override
  public Object visit(UnaryExpression unaryExpression) {
    Object value = unaryExpression.getExpression().accept(this);
    if (isBoolean(value)) {
      throw new IllegalArgumentException("Trying to perform unary operation on boolean value. Was: " + unaryExpression.getOperator().getLexeme() + "" + value);
    }
    if (isInteger(value)) {
      int intValue = Integer.parseInt(value.toString());
      return unaryExpression.getOperator().getTokenType() == TokenType.MINUS ? -intValue : intValue;
    }
    throw new IllegalArgumentException("Wrong operation: " + unaryExpression.getOperator().getTokenType() + value);
  }

  public Object evaluate(Expression expr) {
    return expr.accept(this);
  }

  private boolean isBoolean(Object value) {
    return value instanceof Boolean;
  }

  private boolean isInteger(Object value) {
    try {
      int intValue = Integer.parseInt(value.toString());
      return true;
    } catch (Throwable t) {
      return false;
    }
  }

  @Override
  public Object visit(PrintStatement printStatement) {
    Object value = printStatement.getExpression().accept(this);
    System.out.println(value);
    return value;
  }

  @Override
  public Object visit(AssignStatement assignStatement) {
    Object value = assignStatement.getExpression().accept(this);
    Environment.setGlobalVariable(assignStatement.getIdentifier().getLexeme(), value);
    return value;
  }

  @Override
  public Object visit(StatementGroup statementGroup) {
    for (Statement statement : statementGroup.getStatements()) {
      statement.accept(this);
    }
    return null;
  }
}
