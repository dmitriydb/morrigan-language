package ru.shanalotte.coderun.cache;

import java.util.Optional;
import ru.shanalotte.coderun.api.CodeRunResult;
import ru.shanalotte.coderun.api.CodeRunRequest;

public interface CodeRunCache {

  void cache(CodeRunRequest request, CodeRunResult result);

  boolean contains(CodeRunRequest request);

  Optional<CodeRunResult> get(CodeRunRequest request);

}
