package ru.shanalotte.coderun.cache;

import java.util.HashMap;
import java.util.Map;
import ru.shanalotte.coderun.CodeRunResult;
import ru.shanalotte.coderun.api.CodeRunRequest;

public class RedisCodeRunCache implements CodeRunCache {

  private final Map<CodeRunRequest, CodeRunResult> hashMap = new HashMap<>();

  @Override
  public void cache(CodeRunRequest request, CodeRunResult result) {
      hashMap.put(request, result);
  }

  @Override
  public boolean contains(CodeRunRequest request) {
    return hashMap.containsKey(request);
  }

  @Override
  public CodeRunResult get(CodeRunRequest request) {
    return hashMap.get(request);
  }
}
