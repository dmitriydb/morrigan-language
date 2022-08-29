package ru.shanalotte.scanner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(staticName = "of")
public class Token {
  private String lexeme;
  private TokenType tokenType;
  private int lineNumber;
  private Object literal;
  private int startPosition;
  private int endPosition;
}
