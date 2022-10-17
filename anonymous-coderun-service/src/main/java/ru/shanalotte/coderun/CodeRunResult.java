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

  private final @NonNull List<String> stdout = new ArrayList<>();
  private final @NonNull List<String> stderr = new ArrayList<>();

  public static CodeRunResult of(String message) {
    CodeRunResult codeRunResult = new CodeRunResult();
    codeRunResult.addToStdout(message);
    return codeRunResult;
  }

  public List<String> stdout() {
    return stdout;
  }

  public List<String> stderr() {
    return stderr;
  }

  public void addToStdout(String stdout) {
    this.stdout.add(stdout);
  }

  public void addToStderr(String stderr) {
    this.stderr.add(stderr);
  }
}
