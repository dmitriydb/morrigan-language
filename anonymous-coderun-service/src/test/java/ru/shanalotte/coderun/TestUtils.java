package ru.shanalotte.coderun;

import java.util.List;
import ru.shanalotte.Morrigan;
import ru.shanalotte.coderun.api.CodeRunRequest;
import ru.shanalotte.coderun.api.SimpleCodeRunRequest;
import ru.shanalotte.coderun.api.SupportedLanguage;
import ru.shanalotte.coderun.cache.CodeRunCache;
import ru.shanalotte.coderun.cache.RedisCodeRunCache;

public class TestUtils {

  public static CodeRunRequest codeRequest(String line) {
    return SimpleCodeRunRequest.builder()
        .language(SupportedLanguage.MORRIGAN)
        .code("morrigan remembers what is " + line + ".")
        .build();
  }

  public static CodeRunService codeRunService(Morrigan morrigan, CodeRunCache codeRunCache) {
    return new AnonymousMorriganCodeRunService(morrigan, codeRunCache);
  }

  public static CodeRunService simpleCodeRunService() {
    return new AnonymousMorriganCodeRunService(new Morrigan(), new RedisCodeRunCache());
  }

  public static List<CodeRunRequest> prepareCodeRequestBatch() {
    return List.of(codeRequest("1"), codeRequest("2"), codeRequest("3"));
  }
}
