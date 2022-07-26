package ru.shanalotte.environment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.shanalotte.interpreter.Interpreter;
import ru.shanalotte.interpreter.MorriganCallable;

public class Environment {

  private static Map<String, MorriganCallable> nativeFunctions = new HashMap<>();
  private static Map<String, Object> globalVariables = new HashMap<>();

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


  public static void setGlobalVariable(String variableName, Object value) {
    globalVariables.put(variableName, value);
  }

  public static Object getGlobalVariableValue(String variableName) {
    return globalVariables.getOrDefault(variableName, null);
  }

  public static boolean globalVariableExists(String variableName) {
    return globalVariables.containsKey(variableName);
  }

  public static void clear() {
    globalVariables.clear();
  }

  public static boolean isNativeFunctionExists(String functionName) {
    return nativeFunctions.containsKey(functionName);
  }

  public static MorriganCallable getNativeFunction(String functionName) {
    return nativeFunctions.get(functionName);
  }
}
