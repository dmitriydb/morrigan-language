package ru.shanalotte.coderun;

import java.util.List;
import java.util.UUID;
import static org.mockito.Mockito.mock;
import ru.shanalotte.Morrigan;
import ru.shanalotte.coderun.api.CodeRunRequest;
import ru.shanalotte.coderun.api.AnonymousCodeRunRequest;
import ru.shanalotte.coderun.api.SupportedLanguage;
import ru.shanalotte.coderun.cache.CodeRunCache;
import ru.shanalotte.coderun.cache.RedisCodeRunCache;

public class TestUtils {

  public static CodeRunRequest codeRequest(String line) {
    return AnonymousCodeRunRequest.builder()
        .language(SupportedLanguage.MORRIGAN)
        .code("morrigan remembers what is " + line + ".")
        .build();
  }

  public static CodeRunService codeRunService(CodeRunCache codeRunCache) {
    return new AnonymousMorriganCodeRunService(codeRunCache);
  }

  public static CodeRunService codeRunService(Morrigan morrigan, CodeRunCache codeRunCache) {
    return new AnonymousMorriganCodeRunService(morrigan, codeRunCache);
  }

  public static CodeRunService simpleCodeRunService() {
    return new AnonymousMorriganCodeRunService(new RedisCodeRunCache());
  }

  public static List<CodeRunRequest> prepareCodeRequestBatch() {
    return List.of(codeRequest("1"), codeRequest("2"), codeRequest("3"));
  }

  public static CodeRunRequest randomCodeRequest() {
    var randomValue = "string" + UUID.randomUUID().toString();
    randomValue = randomValue.replace("-", "");
    return codeRequest(randomValue);
  }

  private static void generateRandomString() {

  }

  public static CodeRunService codeRunServiceWithMockedCache() {
    return new AnonymousMorriganCodeRunService(mock(CodeRunCache.class));
  }

  public static CodeRunRequest invalidRequest() {
    return codeRequest(". asd .");
  }
}
