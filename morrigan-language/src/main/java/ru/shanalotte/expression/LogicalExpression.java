package ru.shanalotte.expression;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import ru.shanalotte.parser.Visitor;
import ru.shanalotte.scanner.TokenType;

@Getter
public class LogicalExpression extends Expression{
  private List<Expression> operands = new ArrayList<>();
  private List<TokenType> operators = new ArrayList<>();

  public void addOperand(Expression expression) {
    operands.add(expression);
  }

  public void addOperator(TokenType tokenType) {
    operators.add(tokenType);
  }

  @Override
  public <R> R accept(Visitor<R> visitor) {
    return visitor.visit(this);
  }


}
