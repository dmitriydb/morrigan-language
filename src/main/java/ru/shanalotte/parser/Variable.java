package ru.shanalotte.parser;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Variable {
  private final String name;
  private Object value;
}
