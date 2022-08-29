package ru.shanalotte.coderun;

import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class CodeRunResult {

  private final @NonNull List<String> stdout = new ArrayList<>();

  public List<String> stdout() {
    return stdout;
  }

  public void addToStdout(String stdout) {
    this.stdout.add(stdout);
  }
}
