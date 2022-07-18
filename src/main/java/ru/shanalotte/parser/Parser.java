package ru.shanalotte.parser;

import java.util.List;
import lombok.RequiredArgsConstructor;
import ru.shanalotte.scanner.Token;
import ru.shanalotte.scanner.TokenType;
import static ru.shanalotte.scanner.TokenType.FALSE;
import static ru.shanalotte.scanner.TokenType.IDENTIFIER;
import static ru.shanalotte.scanner.TokenType.MINUS;
import static ru.shanalotte.scanner.TokenType.NUMBER;
import static ru.shanalotte.scanner.TokenType.PLUS;
import static ru.shanalotte.scanner.TokenType.SLASH;
import static ru.shanalotte.scanner.TokenType.STAR;
import static ru.shanalotte.scanner.TokenType.STRING;
import static ru.shanalotte.scanner.TokenType.TRUE;

@RequiredArgsConstructor
public class Parser {
  final List<Token> tokens;
  private int current = 0;

  public Expression parse() {
    return expression();
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
