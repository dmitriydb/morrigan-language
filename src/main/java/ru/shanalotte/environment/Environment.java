package ru.shanalotte.environment;

import java.util.HashMap;
import java.util.Map;

public class Environment {

  private static Map<String, Object> globalVariables = new HashMap<>();

  public static void setGlobalVariable(String variableName, Object value) {
    globalVariables.put(variableName, value);
  }

  public static Object getGlobalVariableValue(String variableName) {
    return globalVariables.getOrDefault(variableName, null);
  }

  public static boolean globalVariableExists(String variableName) {
    return globalVariables.containsKey(variableName);
  }
}
