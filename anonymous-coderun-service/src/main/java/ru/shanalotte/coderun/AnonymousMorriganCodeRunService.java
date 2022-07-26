package ru.shanalotte.coderun;

import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ru.shanalotte.Morrigan;
import ru.shanalotte.coderun.api.CodeRunRequest;
import ru.shanalotte.coderun.api.CodeRunResult;
import ru.shanalotte.coderun.cache.CodeRunCache;
import ru.shanalotte.interpreter.Interpreter;
import ru.shanalotte.scanner.Scanner;

@Slf4j
public class AnonymousMorriganCodeRunService implements CodeRunService {

  private final @NonNull CodeRunCache codeRunCache;
  private volatile Morrigan morrigan;

  public AnonymousMorriganCodeRunService(@NonNull CodeRunCache codeRunCache) {
    this.codeRunCache = codeRunCache;
  }

  public AnonymousMorriganCodeRunService(
      @NonNull Morrigan morrigan, @NonNull CodeRunCache codeRunCache) {
    this.codeRunCache = codeRunCache;
    this.morrigan = morrigan;
  }

  @Override
  public CodeRunResult run(CodeRunRequest request) {
    log.debug("Incoming request {}", request);
    if (codeRunCache.contains(request)) {
      log.debug("Cache already contains request {}", request);
      return codeRunCache.get(request).orElse(CodeRunResult.of(CodeRunResultMessages.TRY_AGAIN));
    }
    CodeRunResult result = new CodeRunResult();
    Morrigan morrigan =
        this.morrigan == null ? new Morrigan(new Interpreter(), new Scanner()) : this.morrigan;
    try {
      morrigan.interpret(request.code());
      morrigan.getInterpreter()
          .getResult()
          .forEach(result::addToStdout);
    } catch (Throwable t) {
      morrigan.getStderr()
          .forEach(result::addToStderr);
    }
    log.debug("Request {} processed, result {}", request, result);
    if (!codeRunCache.contains(request)) {
      log.debug("Caching {} to {}", request, result);
      codeRunCache.cache(request, result);
    }
    return result;
  }

  @Override
  public List<CodeRunResult> batchRun(List<CodeRunRequest> batch) {
    log.debug("Incoming batch request {}", batch);
    List<CodeRunResult> results = new ArrayList<>();
    for (CodeRunRequest request : batch) {
      results.add(run(request));
    }
    log.debug("Batch {} processed, result {}", batch, results);
    return results;
  }

}
