package ru.shanalotte.coderun;

import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
public class CodeRunResult {

  public static CodeRunResult of (String message) {
    CodeRunResult codeRunResult = new CodeRunResult();
    codeRunResult.addToStdout(message);
    return codeRunResult;
  }

  private final @NonNull List<String> stdout = new ArrayList<>();

  public List<String> stdout() {
    return stdout;
  }

  public void addToStdout(String stdout) {
    this.stdout.add(stdout);
  }
}
