package ru.shanalotte.coderun.api;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class SimpleCodeRunRequest implements CodeRunRequest {

  private SupportedLanguage language;
  private String code;

  @Override
  public SupportedLanguage language() {
    return language;
  }

  @Override
  public String code() {
    return code;
  }

}
