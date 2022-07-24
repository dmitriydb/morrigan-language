package ru.shanalotte.parser;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import ru.shanalotte.expression.BinaryExpression;
import ru.shanalotte.expression.Expression;
import ru.shanalotte.expression.Literal;
import ru.shanalotte.expression.UnaryExpression;
import ru.shanalotte.scanner.Token;
import ru.shanalotte.scanner.TokenType;
import static ru.shanalotte.scanner.TokenType.DOT;
import static ru.shanalotte.scanner.TokenType.ELSE;
import static ru.shanalotte.scanner.TokenType.FALSE;
import static ru.shanalotte.scanner.TokenType.IDENTIFIER;
import static ru.shanalotte.scanner.TokenType.IF;
import static ru.shanalotte.scanner.TokenType.IS;
import static ru.shanalotte.scanner.TokenType.MINUS;
import static ru.shanalotte.scanner.TokenType.MORRIGAN;
import static ru.shanalotte.scanner.TokenType.NUMBER;
import static ru.shanalotte.scanner.TokenType.PLUS;
import static ru.shanalotte.scanner.TokenType.REMEMBERS;
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
import ru.shanalotte.statements.IfStatement;
import ru.shanalotte.statements.PrintStatement;
import ru.shanalotte.statements.Statement;
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
    if (match(REMEMBERS)) return printStatement();
    if (match(SAYS)) return expressionStatement();
    throw new IllegalStateException("morrigan what?");
  }

  private Statement printStatement() {
    consume(WHAT, "morrigan remembers what?");
    consume(IS, "morrigan remembers what is what? ");
    Expression value = expression();
    return new PrintStatement(value);
  }

  private Statement expressionStatement() {
    consume(THAT, "morrigan says what?");
    if (match(IF)) {
      return ifStatement();
    }
    if (match(WHILE)) {
      return whileStatement();
    }  else
    {
      Token identifier = consume(IDENTIFIER, "morrigan says that what?");
      consume(IS, "morrigan says that " + identifier.getLexeme() + " what?");
      Expression expression = expression();
      return new AssignStatement(identifier, expression);
    }
  }

  private Statement whileStatement() {
    Expression loopCondition = expression();
    Statement loopStatement = statement();
    return new WhileStatement(loopCondition, loopStatement);
  }

  private Statement ifStatement() {
    Expression condition = expression();
    consume(THEN, "missing <then> after if expression");
    Statement trueBranch = statement();
    Statement falseBranch = null;
    if (match(ELSE)) {
      falseBranch = statement();
    }
    return new IfStatement(condition, trueBranch, falseBranch);
  }


  private Expression expression() {
    return equality();
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
    return primary();
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
