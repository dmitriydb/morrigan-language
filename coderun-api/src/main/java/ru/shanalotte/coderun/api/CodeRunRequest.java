package ru.shanalotte.coderun.api;

public interface CodeRunRequest {
  SupportedLanguage language();
  String code();
}
