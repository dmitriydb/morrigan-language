package ru.shanalotte.exception;

import lombok.Getter;

public class Return extends RuntimeException {
  @Getter
  private final Object value;

  public Return(Object value) {
    super(null, null, false, false);
    this.value = value;
  }
}
