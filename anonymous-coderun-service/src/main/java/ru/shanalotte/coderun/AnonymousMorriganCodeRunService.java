package ru.shanalotte.coderun;

import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.shanalotte.Morrigan;
import ru.shanalotte.coderun.api.CodeRunRequest;

@RequiredArgsConstructor
public class AnonymousMorriganCodeRunService implements CodeRunService {

  private final @NonNull Morrigan morrigan;

  @Override
  public CodeRunResult run(CodeRunRequest codeRunRequest) {
    CodeRunResult codeRunResult = new CodeRunResult();
    morrigan.interpret(codeRunRequest.code());
    morrigan.getInterpreter()
        .getResult()
        .forEach(codeRunResult::addToStdout);
    return codeRunResult;
  }

  @Override
  public List<CodeRunResult> batchRun(List<CodeRunRequest> batch) {
    List<CodeRunResult> results = new ArrayList<>();
    for (CodeRunRequest request : batch) {
      results.add(run(request));
    }
    return results;
  }
}
