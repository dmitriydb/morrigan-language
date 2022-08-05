package ru.shanalotte.environment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.shanalotte.interpreter.Interpreter;
import ru.shanalotte.interpreter.MorriganCallable;

@NoArgsConstructor
public class Environment {

  private static Map<String, MorriganCallable> nativeFunctions = new HashMap<>();
  @Getter
  private Map<String, Object> variables = new HashMap<>();

  static {
    nativeFunctions.put("concat", new MorriganCallable() {
      @Override
      public Object call(Interpreter interpreter, List<Object> arguments) {
        StringBuilder result = new StringBuilder();
        for (Object argument : arguments) {
          result.append(String.valueOf(argument));
        }
        return result.toString();
      }

      @Override
      public int arity() {
        return 2;
      }
    });
  }



  public Environment(Environment original) {
    for (String variable : original.getVariables().keySet()) {
      this.setVariable(variable, original.getVariables().get(variable));
    }
  }


  public void setVariable(String variableName, Object value) {
    variables.put(variableName, value);
  }

  public Object getVariableValue(String variableName) {
    return variables.getOrDefault(variableName, null);
  }

  public boolean variableExists(String variableName) {
    return variables.containsKey(variableName);
  }

  public  void clear() {
    variables.clear();
  }

  public static boolean isNativeFunctionExists(String functionName) {
    return nativeFunctions.containsKey(functionName);
  }

  public static MorriganCallable getNativeFunction(String functionName) {
    return nativeFunctions.get(functionName);
  }

  public static void addFunction(String functionName, MorriganCallable morriganCallable) {
    nativeFunctions.put(functionName, morriganCallable);
  }

}
