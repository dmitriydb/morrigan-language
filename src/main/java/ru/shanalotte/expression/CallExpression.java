package ru.shanalotte.expression;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.shanalotte.parser.Visitor;
import ru.shanalotte.scanner.Token;

@Getter
@Setter
@RequiredArgsConstructor
public class CallExpression extends Expression{
  private final Expression callee;
  private final Token parenthesis;
  private final List<Expression> arguments;

  @Override
  public <R> R accept(Visitor<R> visitor) {
    return visitor.visit(this);
  }
}

