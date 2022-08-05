package ru.shanalotte.interpreter;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.shanalotte.environment.Environment;
import ru.shanalotte.statements.FunctionDeclarationStatement;

@RequiredArgsConstructor
public class MorriganFunction implements MorriganCallable{

  private final @NonNull FunctionDeclarationStatement declarationStatement;

  @Override
  public Object call(Interpreter interpreter, List<Object> arguments) {
    Environment environment = new Environment(interpreter.getEnvironment());
    for (int i = 0; i < declarationStatement.getParameters().size(); i++) {
      String argument = declarationStatement.getParameters().get(i).getLexeme();
      Object value = arguments.get(i);
      environment.setVariable(argument, value);
    }
    Environment originalEnvironment = interpreter.getEnvironment();
    try {
      interpreter.setEnvironment(environment);
      interpreter.evaluate(declarationStatement.getFunctionBody());
    }
    finally {
      interpreter.setEnvironment(originalEnvironment);
    }
    return null;
  }

  @Override
  public int arity() {
    return declarationStatement.getParameters().size();
  }
}
