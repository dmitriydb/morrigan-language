package ru.shanalotte.interpreter;

import java.util.List;

public interface MorriganCallable {
  Object call(Interpreter interpreter, List<Object> arguments);
  int arity();
}
