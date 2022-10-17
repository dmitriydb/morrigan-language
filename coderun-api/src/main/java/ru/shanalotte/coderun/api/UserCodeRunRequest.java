package ru.shanalotte.coderun.api;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class UserCodeRunRequest implements CodeRunRequest {

  private @NonNull SupportedLanguage language;
  private @NonNull String code;
  private @NonNull String username = UsernameConstants.USERNAME_NOT_SET;

  public UserCodeRunRequest(@NonNull SupportedLanguage language, @NonNull String code) {
    this.language = language;
    this.code = code;
  }

  public UserCodeRunRequest(@NonNull SupportedLanguage language,
                            @NonNull String code, @NonNull String username) {
    this.language = language;
    this.code = code;
    this.username = username;
  }

  @Override
  public SupportedLanguage language() {
    return language;
  }

  @Override
  public String code() {
    return code;
  }

  @Override
  public String username() {
    return username;
  }
}
