package ru.shanalotte.interpreter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import ru.shanalotte.environment.Environment;
import ru.shanalotte.exception.Return;
import ru.shanalotte.expression.BinaryExpression;
import ru.shanalotte.expression.CallExpression;
import ru.shanalotte.expression.Expression;
import ru.shanalotte.expression.Literal;
import ru.shanalotte.expression.LogicalExpression;
import ru.shanalotte.expression.UnaryExpression;
import ru.shanalotte.parser.Visitor;
import ru.shanalotte.scanner.Token;
import ru.shanalotte.scanner.TokenType;
import ru.shanalotte.statements.AssignStatement;
import ru.shanalotte.statements.CallStatement;
import ru.shanalotte.statements.FunctionDeclarationStatement;
import ru.shanalotte.statements.IfStatement;
import ru.shanalotte.statements.PrintStatement;
import ru.shanalotte.statements.ReturnStatement;
import ru.shanalotte.statements.Statement;
import ru.shanalotte.statements.StatementGroup;
import ru.shanalotte.statements.WhileStatement;

public class Interpreter implements Visitor<Object> {

  @Getter
  private final List<String> result = new ArrayList<>();

  @Getter
  @Setter
  private Environment environment = new Environment();

  private String numberToString(Object value) {
    if (value instanceof String) {
      return (String) value;
    }
    if (value instanceof Boolean) {
      return String.valueOf((boolean) value);
    }
    boolean isInteger = (double) value % 1 == 0;
    if (isInteger) {
      return String.valueOf(Double.valueOf((double) value).intValue());
    } else {
      return String.valueOf((double) value);
    }

  }

  @Override
  public Object visit(BinaryExpression binaryExpression) {
    Object leftValue = binaryExpression.getLeftSide().accept(this);
    Object rightValue = binaryExpression.getRightSide().accept(this);
    try {
      switch (binaryExpression.getOperator().getTokenType()) {
        case PLUS:
          if (isNumber(leftValue) && isNumber((rightValue))) {
            return (double) leftValue + (double) rightValue;
          } else {
            String result = "";
            result += numberToString(leftValue);
            result += numberToString(rightValue);
            return result;
          }
        case MINUS:
          if (bothNotNumbers(leftValue, rightValue)) {
            throw runtimeError(binaryExpression.getOperator(), "Substracting strings/booleans is illegal: " + leftValue.toString() + ", " + rightValue.toString());
          }
          return (double) leftValue - (double) rightValue;
        case STAR:
          if (bothNotNumbers(leftValue, rightValue)) {
            throw runtimeError(binaryExpression.getOperator(), "Multiplying strings/booleans is illegal: " + leftValue.toString() + ", " + rightValue.toString());
          }
          return (double) leftValue * (double) rightValue;
        case SLASH:
          if (bothNotNumbers(leftValue, rightValue)) {
            throw runtimeError(binaryExpression.getOperator(), "Dividing strings/booleans is illegal: " + leftValue.toString() + ", " + rightValue.toString());
          }
          return (double) leftValue / (double) rightValue;
        case MORE:
          return isMore(leftValue, rightValue);
        case LESS:
          return !isMore(leftValue, rightValue) && !isEquals(leftValue, rightValue);
        case EQUALS:
          return isEquals(leftValue, rightValue);
        default:
          return 0;
      }
    } catch (Throwable t) {
      throw runtimeError(binaryExpression.getOperator(), t.getMessage());
    }
  }

  private boolean isMore(Object a, Object b) {
    if (isBoolean(a) && isBoolean(b)) {
      throw new IllegalArgumentException("It is illegal to compare two boolean values. Were: " + a + ", " + b);
    }
    if (!bothNotNumbers(a, b)) {
      String sa = String.valueOf(a);
      String sb = String.valueOf(b);
      sa = sa.replace(",", ".");
      sb = sb.replace(",", ".");
      return Double.parseDouble(sa) > Double.parseDouble(sb);
    }
    System.out.println("2222");
    return String.valueOf(a).compareTo(String.valueOf(b)) > 0;
  }

  private boolean isEquals(Object a, Object b) {
    if (isBoolean(a) && isBoolean(b)) {
      return (a.equals(b));
    }
    return !isMore(a, b) && !isMore(b, a);
  }

  private boolean bothNotNumbers(Object leftValue, Object rightValue) {
    return !isNumber(leftValue) || !isNumber(rightValue);
  }

  @Override
  public Object visit(Literal literal) {
    if (literal.getLiteral().equals("true") || literal.getLiteral().equals("false")) {
      return Boolean.valueOf(literal.getLiteral().toString());
    }
    if (environment.variableExists(literal.getLiteral().toString())) {
      return environment.getVariableValue(literal.getLiteral().toString());
    }
    try {
      String stringValue = literal.getLiteral().toString();
      stringValue = stringValue.replace(",", ".");
      return Double.parseDouble(stringValue);
    } catch (Throwable t) {
      return literal.getLiteral().toString();
    }
  }

  @Override
  public Object visit(IfStatement ifStatement) {
    boolean conditionResult = (boolean) ifStatement.getCondition().accept(this);
    if (conditionResult) {
      return ifStatement.getTrueBranch().accept(this);
    } else {
      if (ifStatement.getFalseBranch() != null) {
        return ifStatement.getFalseBranch().accept(this);
      } else {
        return null;
      }
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
      throw runtimeError(unaryExpression.getOperator(), "Trying to perform unary operation on boolean value. Was: " + unaryExpression.getOperator().getLexeme() + "" + value);
    }
    if (isNumber(value)) {
      double doubleValue = Double.parseDouble(value.toString());
      return unaryExpression.getOperator().getTokenType() == TokenType.MINUS ? -doubleValue : doubleValue;
    }
    throw runtimeError(unaryExpression.getOperator(), "Wrong operation: " + unaryExpression.getOperator().getTokenType() + value);
  }

  public Object evaluate(Expression expr) {
    return expr.accept(this);
  }

  private boolean isBoolean(Object value) {
    return value instanceof Boolean;
  }

  private boolean isNumber(Object value) {
    try {
      String stringValue = value.toString();
      stringValue = stringValue.replace(",", ".");
      double doubleValue = Double.parseDouble(stringValue);
      return true;
    } catch (Throwable t) {
      return false;
    }
  }

  @Override
  public Object visit(PrintStatement printStatement) {
    Object value = printStatement.getExpression().accept(this);
    System.out.println(value);
    if (value != null) {
      result.add(value.toString());
    }
    return value;
  }

  @Override
  public Object visit(AssignStatement assignStatement) {
    Object value = assignStatement.getExpression().accept(this);
    environment.setVariable(assignStatement.getIdentifier().getLexeme(), value);
    return value;
  }

  @Override
  public Object visit(StatementGroup statementGroup) {
    for (Statement statement : statementGroup.getStatements()) {
      statement.accept(this);
    }
    return null;
  }

  @Override
  public Object visit(LogicalExpression logicalExpression) {
    Object firstOperand = this.evaluate(logicalExpression.getOperands().get(0));
    if (logicalExpression.getOperands().size() == 1) {
      return firstOperand;
    } else {
      Iterator<TokenType> operators = logicalExpression.getOperators().iterator();
      for (int i = 1; i < logicalExpression.getOperands().size(); i++) {
        Object secondOperand = this.evaluate(logicalExpression.getOperands().get(i));
        firstOperand = evaluate(firstOperand, secondOperand, operators.next());
      }
    }
    return firstOperand;
  }

  private Object evaluate(Object firstOperand, Object secondOperand, TokenType operator) {
    if (!isBoolean(firstOperand) || !isBoolean(secondOperand)) {
      throw new IllegalArgumentException("Both operands should be logical values in logical expression");
    }
    if (operator == TokenType.LOGICAL_OR) {
      return (boolean) firstOperand || (boolean) secondOperand;
    } else if (operator == TokenType.LOGICAL_AND) {
      return (boolean) firstOperand && (boolean) secondOperand;
    } else {
      throw new IllegalArgumentException("Wrong logical operator in logical expression: " + operator);
    }
  }

  @Override
  public Object visit(CallExpression callExpression) {
    Object callee = evaluate(callExpression.getCallee());
    List<Object> arguments = new ArrayList<>();
    for (Expression argument : callExpression.getArguments()) {
      arguments.add(this.evaluate(argument));
    }
    if (Environment.isNativeFunctionExists((String) callee)) {
      callee = Environment.getNativeFunction((String) callee);
    }
    if (!(callee instanceof MorriganCallable)) {
      throw runtimeError(callExpression.getParenthesis(), "Can call only functions");
    }
    MorriganCallable function = (MorriganCallable) callee;
    if (function.arity() != arguments.size()) {
      throw runtimeError(callExpression.getParenthesis(), "Expected function arity is " + function.arity() + ", but got: " + arguments.size());
    }
    return function.call(this, arguments);
  }

  @Override
  public Object visit(FunctionDeclarationStatement functionDeclarationStatement) {
    String functionName = functionDeclarationStatement.getFunctionName();
    MorriganFunction morriganFunction = new MorriganFunction(functionDeclarationStatement);
    Environment.addFunction(functionName, morriganFunction);
    return null;
  }

  @Override
  public Object visit(CallStatement callStatement) {
    return this.evaluate(callStatement.getFunctionCall());
  }

  @Override
  public Object visit(ReturnStatement returnStatement) {
    Object returnValue = evaluate(returnStatement.getReturnExpression());
    throw new Return(returnValue);
  }

  public void clearResults() {
    this.result.clear();
  }

  private Error runtimeError(int lineNumber, int position, String message) {
    return new Error(String.format("Runtime error at line %d position %d: %s", lineNumber, position, message));
  }

  private Error runtimeError(Token token, String message) {
    if (token == null) {
      return new Error(String.format("Runtime error : %s", message));
    } else {
      return new Error(String.format("Runtime error at line %d position %d: %s", token.getLineNumber(), token.getStartPosition(), message));
    }

  }

}
