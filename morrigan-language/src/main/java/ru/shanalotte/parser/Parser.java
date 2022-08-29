package ru.shanalotte.parser;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import ru.shanalotte.expression.BinaryExpression;
import ru.shanalotte.expression.CallExpression;
import ru.shanalotte.expression.Expression;
import ru.shanalotte.expression.Literal;
import ru.shanalotte.expression.LogicalExpression;
import ru.shanalotte.expression.UnaryExpression;
import ru.shanalotte.scanner.Token;
import ru.shanalotte.scanner.TokenType;
import static ru.shanalotte.scanner.TokenType.AND;
import static ru.shanalotte.scanner.TokenType.CALLS;
import static ru.shanalotte.scanner.TokenType.COMMA;
import static ru.shanalotte.scanner.TokenType.DOT;
import static ru.shanalotte.scanner.TokenType.ELSE;
import static ru.shanalotte.scanner.TokenType.FALSE;
import static ru.shanalotte.scanner.TokenType.FUNCTION;
import static ru.shanalotte.scanner.TokenType.IDENTIFIER;
import static ru.shanalotte.scanner.TokenType.IF;
import static ru.shanalotte.scanner.TokenType.IS;
import static ru.shanalotte.scanner.TokenType.LEFT_BRACKET;
import static ru.shanalotte.scanner.TokenType.LEFT_FIGURE_BRACKET;
import static ru.shanalotte.scanner.TokenType.LOGICAL_AND;
import static ru.shanalotte.scanner.TokenType.LOGICAL_OR;
import static ru.shanalotte.scanner.TokenType.MINUS;
import static ru.shanalotte.scanner.TokenType.MORRIGAN;
import static ru.shanalotte.scanner.TokenType.NUMBER;
import static ru.shanalotte.scanner.TokenType.PLUS;
import static ru.shanalotte.scanner.TokenType.REMEMBERS;
import static ru.shanalotte.scanner.TokenType.RETURNS;
import static ru.shanalotte.scanner.TokenType.RIGHT_BRACKET;
import static ru.shanalotte.scanner.TokenType.RIGHT_FIGURE_BRACKET;
import static ru.shanalotte.scanner.TokenType.SAYS;
import static ru.shanalotte.scanner.TokenType.SLASH;
import static ru.shanalotte.scanner.TokenType.STAR;
import static ru.shanalotte.scanner.TokenType.STRING;
import static ru.shanalotte.scanner.TokenType.THAT;
import static ru.shanalotte.scanner.TokenType.THEN;
import static ru.shanalotte.scanner.TokenType.TRUE;
import static ru.shanalotte.scanner.TokenType.WHAT;
import static ru.shanalotte.scanner.TokenType.WHILE;
import ru.shanalotte.statements.AssignStatement;
import ru.shanalotte.statements.CallStatement;
import ru.shanalotte.statements.FunctionDeclarationStatement;
import ru.shanalotte.statements.IfStatement;
import ru.shanalotte.statements.PrintStatement;
import ru.shanalotte.statements.ReturnStatement;
import ru.shanalotte.statements.Statement;
import ru.shanalotte.statements.StatementGroup;
import ru.shanalotte.statements.WhileStatement;

@RequiredArgsConstructor
public class Parser {
  final List<Token> tokens;
  private int current = 0;

  private Token consume(TokenType type, String message) {
    if (check(type)) return advance();
    throw new Error(message);
  }

  public Expression parseExpression() {
    return expression();
  }

  public List<Statement> parse() {
    List<Statement> statements = new ArrayList<>();
    while (!isAtEnd()) {
      Statement nextStatement = statement();
      statements.add(nextStatement);
      if (match(TokenType.X)) {
        consume(NUMBER, "loop amount should be a number.");
        int times = Integer.parseInt(String.valueOf(previous().getLiteral()));
        for (int i = 0; i < times - 1; i++) {
          statements.add(nextStatement);
        }
      }
      consume(DOT, "please, do not forget the '.'");
    }
    return statements;
  }

  private Statement statement() {
    consume(MORRIGAN, "what?");
    if (match(CALLS)) return callStatement();
    if (match(RETURNS)) return returnStatement();
    if (match(REMEMBERS)) return printStatement();
    if (match(SAYS)) {
      consume(THAT, "morrigan says what?");
      if (match(IF)) {
        return ifStatement();
      }
      if (match(WHILE)) {
        return whileStatement();
      }
      return declarationStatement();
    };
    throw new IllegalStateException("morrigan what?");
  }

  private ReturnStatement returnStatement() {
    Expression returnExpression = expression();
    return new ReturnStatement(returnExpression);
  }

  private Statement printStatement() {
    consume(WHAT, "morrigan remembers what?");
    consume(IS, "morrigan remembers what is what? ");
    Expression value = expression();
    return new PrintStatement(value);
  }

  private CallStatement callStatement() {
    Expression call = call();
    return new CallStatement(call);
  }

  private Statement declarationStatement() {
    Token identifier = consume(IDENTIFIER, "morrigan says that what?");
      consume(IS, "morrigan says that " + identifier.getLexeme() + " what?");
      if (match(FUNCTION)) {
        return functionDeclaration(identifier);
      }
      Expression expression = expression();
      if (!match(COMMA)) {
        return new AssignStatement(identifier, expression);
      }
      return assignmentGroup(new AssignStatement(identifier, expression));
  }

  private Statement functionDeclaration(Token functionName) {
    consume(LEFT_BRACKET, "missing ( after function declaration");
    List<Token> parameters = match(RIGHT_BRACKET) ? new ArrayList<>() : parameters();
    consume(LEFT_FIGURE_BRACKET, "missing { after function parameters");
    Statement statementGroup = statementGroup();
    consume(RIGHT_FIGURE_BRACKET, "missing } after function body");
    return new FunctionDeclarationStatement(functionName.getLexeme(), parameters, statementGroup);
  }

  private List<Token> parameters() {
    List<Token> parameters = new ArrayList<>();
    while(true) {
      consume(IDENTIFIER, "missing parameter name in function parameter list");
      parameters.add(previous());
      if (match(RIGHT_BRACKET)) break;
      if (match(COMMA)) continue;
      if (isAtEnd()) {
        throw new IllegalStateException("Missing ) in function declaration while EOF");
      }
    }
    return parameters;
  }

  private Statement assignmentGroup(Statement firstStatement) {
    StatementGroup statementGroup = new StatementGroup();
    statementGroup.addStatement(firstStatement);
    do {
      Token identifier = consume(IDENTIFIER, "morrigan says that what?");
      consume(IS, "morrigan says that " + identifier.getLexeme() + " what?");
      Expression expression = expression();
      statementGroup.addStatement(new AssignStatement(identifier, expression));
    } while (match(COMMA));
    return statementGroup;
  }

  private Statement whileStatement() {
    Expression loopCondition = expression();
    Statement loopStatement = statement();
    if (match(AND)) {
      loopStatement = statementGroup(loopStatement);
    }
    return new WhileStatement(loopCondition, loopStatement);
  }

  private Statement ifStatement() {
    Expression condition = expression();
    consume(THEN, "missing <then> after if expression");
    Statement trueBranch = statement();
    if (match(AND)) {
      trueBranch = statementGroup(trueBranch);
    }
    Statement falseBranch = null;
    if (match(ELSE)) {
      falseBranch = statement();
      if (match(AND)) {
        falseBranch = statementGroup(falseBranch);
      }
    }
    return new IfStatement(condition, trueBranch, falseBranch);
  }

  private Statement statementGroup(Statement firstStatement) {
    StatementGroup statementGroup = new StatementGroup();
    statementGroup.addStatement(firstStatement);
    do {
      Statement nextTrueBranchStatement = statement();
      statementGroup.addStatement(nextTrueBranchStatement);
    } while (match(AND));
    return statementGroup;
  }

  private Statement statementGroup() {
    StatementGroup statementGroup = new StatementGroup();
    do {
      Statement nextTrueBranchStatement = statement();
      statementGroup.addStatement(nextTrueBranchStatement);
    } while (match(AND));
    return statementGroup;
  }

  private Expression expression() {
    Expression expression = equality();
    if (match(LOGICAL_OR, LOGICAL_AND)) {
      return logicalExpression(expression, previous());
    } else {
      return expression;
    }
  }

  private Expression logicalExpression(Expression firstOperand, Token firstOperator) {
    LogicalExpression logicalExpression = new LogicalExpression();
    logicalExpression.addOperand(firstOperand);
    logicalExpression.addOperator(firstOperator.getTokenType());
    Expression nextOperand = expression();
    logicalExpression.addOperand(nextOperand);
    while (match(LOGICAL_OR, LOGICAL_AND)) {
      logicalExpression.addOperator(previous().getTokenType());
      logicalExpression.addOperand(expression());
    }
    return logicalExpression;
  }

  private Expression equality() {
    Expression expression = comparison();
    while (match(TokenType.EQUALS)) {
      Token operator = previous();
      Expression rightSide = comparison();
      expression = new BinaryExpression(expression, operator, rightSide);
    }
    return expression;
  }

  private Expression comparison() {
    Expression expr = term();
    while (match(TokenType.LESS, TokenType.MORE)) {
      Token operator = previous();
      Expression right = term();
      expr = new BinaryExpression(expr, operator, right);
    }
    return expr;
  }

  private Expression term() {
    Expression expr = factor();
    while (match(MINUS, PLUS)) {
      Token operator = previous();
      Expression right = factor();
      expr = new BinaryExpression(expr, operator, right);
    }
    return expr;
  }

  private boolean check(TokenType type) {
    if (isAtEnd()) return false;
    return peek().getTokenType() == type;
  }

  private Expression factor() {
    Expression expr = unary();
    while (match(SLASH, STAR)) {
      Token operator = previous();
      Expression right = unary();
      expr = new BinaryExpression(expr, operator, right);
    }
    return expr;
  }

  private Expression unary() {
    if (match(MINUS, PLUS)) {
      Token operator = previous();
      Expression right = unary();
      return new UnaryExpression(operator, right);
    }
    return call();
  }

  private Expression call() {
    Expression primary = primary();
    if (match(LEFT_BRACKET)) {
      List<Expression> arguments = match(RIGHT_BRACKET) ? new ArrayList<>() : arguments();
      return new CallExpression(primary, null, arguments);
    } else {
      return primary;
    }
  }

  private List<Expression> arguments() {
    List<Expression> arguments = new ArrayList<>();
    while(true) {
      arguments.add(expression());
      if (match(RIGHT_BRACKET)) break;
      if (match(COMMA)) continue;
      if (isAtEnd()) {
        throw new IllegalStateException("Missing ) in function call while EOF");
      }
    }
    return arguments;
  }

  private Expression primary() {
    if (match(NUMBER, STRING, IDENTIFIER, FALSE, TRUE)) {
      return new Literal(previous().getLiteral());
    } else return null;
  }

  private Token advance() {
    if (!isAtEnd()) current++;
    return previous();
  }

  private boolean match(TokenType... types) {
    for (TokenType type : types) {
      if (check(type)) {
        advance();
        return true;
      }
    }
    return false;
  }

  private boolean isAtEnd() {
    return current >= tokens.size();
  }

  private Token peek() {
    return tokens.get(current);
  }

  private Token previous() {
    return tokens.get(current - 1);
  }
}
