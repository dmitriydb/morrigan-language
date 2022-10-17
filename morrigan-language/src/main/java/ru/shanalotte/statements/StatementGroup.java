package ru.shanalotte.statements;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import ru.shanalotte.parser.Visitor;

@Getter
public class StatementGroup extends Statement {
  private List<Statement> statements = new ArrayList<>();

  @Override
  public <R> R accept(Visitor<R> visitor) {
    return visitor.visit(this);
  }

  public void addStatement(Statement statement) {
    statements.add(statement);
  }
}
