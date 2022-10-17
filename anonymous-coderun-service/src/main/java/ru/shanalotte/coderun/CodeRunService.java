package ru.shanalotte.coderun;

import java.util.List;
import ru.shanalotte.coderun.api.CodeRunRequest;

public interface CodeRunService {

  CodeRunResult run(CodeRunRequest codeRunRequest);

  List<CodeRunResult> batchRun(List<CodeRunRequest> batch);

}
