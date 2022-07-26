package ru.shanalotte.scanner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Scanner {

  public static Map<String, TokenType> keywords = new HashMap<>();

  static {
    keywords.put("is", TokenType.IS);
    keywords.put("or", TokenType.OR);
    keywords.put("and", TokenType.AND);
    keywords.put("morrigan", TokenType.MORRIGAN);
    keywords.put("thinks", TokenType.THINKS);
    keywords.put("says", TokenType.SAYS);
    keywords.put("that", TokenType.THAT);
    keywords.put("wants", TokenType.WANTS);
    keywords.put("to", TokenType.TO);
    keywords.put("know", TokenType.KNOW);
    keywords.put("if", TokenType.IF);
    keywords.put("remembers", TokenType.REMEMBERS);
    keywords.put("what", TokenType.WHAT);
    keywords.put("print", TokenType.PRINT);
    keywords.put("then", TokenType.THEN);
    keywords.put("else", TokenType.ELSE);
    keywords.put("x", TokenType.X);
    keywords.put("while", TokenType.WHILE);
    keywords.put("function", TokenType.FUNCTION);
  }

  private int startIndex = 0;
  private int currentIndex = 0;
  private Line currentLine;

  public List<Token> scan(File file) throws IOException {
    List<String> source = Files.readAllLines(file.toPath());
    List<Line> lines = IntStream.rangeClosed(1, source.size())
        .mapToObj(lineNumber -> Line.of(lineNumber, source.get(lineNumber - 1)))
        .collect(Collectors.toList());
    List<Token> tokens = new ArrayList<>();
    for (Line line : lines) {
      tokens.addAll(scanLine(line));
    }
    return tokens;
  }

  public List<Token> scan(String codeLine) {
    List<String> source = Collections.singletonList(codeLine);
    List<Line> lines = IntStream.rangeClosed(1, source.size())
        .mapToObj(lineNumber -> Line.of(lineNumber, source.get(lineNumber - 1)))
        .collect(Collectors.toList());
    List<Token> tokens = new ArrayList<>();
    for (Line line : lines) {
      tokens.addAll(scanLine(line));
    }
    return tokens;
  }

  private char advance() {
    currentIndex++;
    return currentLine.getCode().charAt(currentIndex - 1);
  }

  private char peek() {
    if (currentIndex >= currentLine.getCode().length()) {
      return '\0';
    }
    return currentLine.getCode().charAt(currentIndex);
  }

  private List<Token> scanLine(Line line) {
    List<Token> tokensInLine = new ArrayList<>();
    currentLine = line;
    startIndex = 0;
    currentIndex = 0;
    while (currentIndex < currentLine.getCode().length()) {
      char nextChar = advance();
      switch (nextChar) {
        case '[':
          skipToken();
          while (peek() != ']') {
            advance();
          }
          tokensInLine.add(newToken(TokenType.LITERAL_STRING));
          break;
        case ']':
          skipToken();
          break;
        case '.':
          tokensInLine.add(newToken(TokenType.DOT));
          break;
        case '+':
          tokensInLine.add(newToken(TokenType.PLUS));
          break;
        case '-':
          tokensInLine.add(newToken(TokenType.MINUS));
          break;
        case '*':
          tokensInLine.add(newToken(TokenType.STAR));
          break;
        case '/':
          tokensInLine.add(newToken(TokenType.SLASH));
          break;
        case ',':
          tokensInLine.add(newToken(TokenType.COMMA));
          break;
        case '|':
          tokensInLine.add(newToken(TokenType.LOGICAL_OR));
          break;
        case '{':
          tokensInLine.add(newToken(TokenType.LEFT_FIGURE_BRACKET));
          break;
        case '}':
          tokensInLine.add(newToken(TokenType.RIGHT_FIGURE_BRACKET));
          break;
        case '&':
          tokensInLine.add(newToken(TokenType.LOGICAL_AND));
          break;
        case '(':
          tokensInLine.add(newToken(TokenType.LEFT_BRACKET));
          break;
        case ')':
          tokensInLine.add(newToken(TokenType.RIGHT_BRACKET));
          break;
        case ';':
          tokensInLine.add(newToken(TokenType.SEMICOLON));
          break;
        case '=':
          tokensInLine.add(newToken(TokenType.EQUALS));
          break;
        case '<':
          tokensInLine.add(newToken(TokenType.LESS));
          break;
        case '>':
          tokensInLine.add(newToken(TokenType.MORE));
          break;
        case ' ':
          skipToken();
          break;
        default:
          if (isLetter(nextChar)) {
            while (isLetter(peek()) || isDigit(peek())) {
              advance();
            }
            tokensInLine.add(newToken(TokenType.STRING));
          } else if (isDigit(nextChar)) {
            while (isDigit(peek())) {
              advance();
            }
            tokensInLine.add(newToken(TokenType.NUMBER));
          } else {
            alertError();
          }

      }
    }
    return tokensInLine;
  }

  private void alertError() {
    System.out.println("Error at position " + currentIndex + " at line " + currentLine.getNumber());
    System.out.println("Unknown character: " + currentLine.getCode().charAt(currentIndex - 1));
    startIndex = currentIndex;
  }


  private void skipToken() {
    startIndex = currentIndex;
  }


  private Token newToken(TokenType tokenType) {
    String lexeme = currentLine.getCode().substring(startIndex, currentIndex);
    if (tokenType == TokenType.LITERAL_STRING) {
      tokenType = TokenType.STRING;
    } else
    if (tokenType == TokenType.STRING) {
      if (isKeyword(lexeme)) {
        tokenType = determineWhatKeywordTokenTypeByKeywordLexeme(lexeme);
      } else if (lexeme.equals("true")) {
        tokenType = TokenType.TRUE;
      } else if (lexeme.equals("false")) {
        tokenType = TokenType.FALSE;
      } else {
        tokenType = TokenType.IDENTIFIER;
      }
    }
    Token token = Token.builder()
        .tokenType(tokenType)
        .lexeme(lexeme)
        .startPosition(startIndex)
        .endPosition(currentIndex)
        .literal(lexeme)
        .lineNumber(currentLine.getNumber())
        .build();
    startIndex = currentIndex;
    return token;
  }

  private boolean isDigit(char c) {
    return c >= '0' && c <= '9';
  }

  private boolean isLetter(char c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
  }

  private boolean isKeyword(String lexeme) {
    return keywords.containsKey(lexeme.toLowerCase());
  }

  private TokenType determineWhatKeywordTokenTypeByKeywordLexeme(String lexeme) {
    return keywords.get(lexeme.toLowerCase());
  }

}
