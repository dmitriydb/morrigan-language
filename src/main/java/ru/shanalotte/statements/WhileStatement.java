package ru.shanalotte.statements;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import ru.shanalotte.expression.Expression;
import ru.shanalotte.parser.Visitor;

@Getter
@RequiredArgsConstructor
public class WhileStatement extends Statement{

  private final Expression loopCondition;
  private final Statement loopStatement;

  @Override
  public <R> R accept(Visitor<R> visitor) {
    return visitor.visit(this);
  }
}
