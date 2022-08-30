package ru.shanalotte.coderun;

import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.shanalotte.Morrigan;
import ru.shanalotte.coderun.api.CodeRunRequest;
import ru.shanalotte.coderun.cache.CodeRunCache;

@RequiredArgsConstructor
public class AnonymousMorriganCodeRunService implements CodeRunService {

  private final @NonNull Morrigan morrigan;
  private final @NonNull CodeRunCache codeRunCache;

  @Override
  public CodeRunResult run(CodeRunRequest request) {
    if (codeRunCache.contains(request)) {
      return codeRunCache.get(request).orElse(CodeRunResult.of(CodeRunResultMessages.TRY_AGAIN));
    }
    CodeRunResult result = new CodeRunResult();
    try {
      morrigan.interpret(request.code());
      morrigan.getInterpreter()
          .getResult()
          .forEach(result::addToStdout);
    } catch (Throwable t) {
      morrigan.getStderr()
          .forEach(result::addToStderr);
    }
    if (!codeRunCache.contains(request)) {
      codeRunCache.cache(request, result);
    }
    return result;
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
