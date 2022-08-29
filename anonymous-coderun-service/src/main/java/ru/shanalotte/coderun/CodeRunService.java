package ru.shanalotte.coderun;

import ru.shanalotte.coderun.api.CodeRunRequest;

public interface CodeRunService {
  CodeRunResult run(CodeRunRequest codeRunRequest);
}
