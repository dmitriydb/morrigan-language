package ru.shanalotte.scanner;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class Line {
  private int number;
  private String code;
}
