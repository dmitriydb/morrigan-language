package ru.shanalotte.parser;

import ru.shanalotte.expression.BinaryExpression;
import ru.shanalotte.expression.CallExpression;
import ru.shanalotte.expression.Literal;
import ru.shanalotte.expression.LogicalExpression;
import ru.shanalotte.expression.UnaryExpression;
import ru.shanalotte.statements.AssignStatement;
import ru.shanalotte.statements.FunctionDeclarationStatement;
import ru.shanalotte.statements.IfStatement;
import ru.shanalotte.statements.PrintStatement;
import ru.shanalotte.statements.StatementGroup;
import ru.shanalotte.statements.WhileStatement;

public interface Visitor<R> {
  R visit(BinaryExpression binaryExpression);
  R visit(Literal literal);
  R visit(UnaryExpression unaryExpression);
  R visit(PrintStatement printStatement);
  R visit(AssignStatement assignStatement);
  R visit(IfStatement ifStatement);
  R visit(WhileStatement whileStatement);
  R visit(StatementGroup statementGroup);
  R visit(LogicalExpression logicalExpression);
  R visit(CallExpression callExpression);
  R visit(FunctionDeclarationStatement functionDeclarationStatement);
}
