package ru.shanalotte.coderun;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import org.mockito.Mockito;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import ru.shanalotte.Morrigan;
import ru.shanalotte.coderun.api.CodeRunRequest;
import ru.shanalotte.coderun.cache.CodeRunCache;
import ru.shanalotte.coderun.cache.RedisCodeRunCache;

public class CachingTest {

  private CodeRunCache mockedCodeRunCache = Mockito.mock(CodeRunCache.class);
  private CodeRunService codeRunService = TestUtils.codeRunService(new Morrigan(), mockedCodeRunCache);

  @BeforeEach
  public void setUp() {
    mockedCodeRunCache = Mockito.mock(CodeRunCache.class);
    codeRunService = TestUtils.codeRunService(new Morrigan(), mockedCodeRunCache);
  }

  @Test
  public void should_CacheCodeRun() {
    CodeRunRequest request = TestUtils.codeRequest("test");
    ArgumentCaptor<CodeRunRequest> requestCaptor = ArgumentCaptor.forClass(CodeRunRequest.class);
    ArgumentCaptor<CodeRunResult> resultCaptor = ArgumentCaptor.forClass(CodeRunResult.class);

    CodeRunResult actualResult = codeRunService.run(request);

    verify(mockedCodeRunCache, Mockito.atLeast(1)).cache(requestCaptor.capture(), resultCaptor.capture());
    assertThat(requestCaptor.getValue().equals(request)).isTrue();
    assertThat(resultCaptor.getValue()).isEqualTo(actualResult);
  }

  @Test
  public void should_CacheCodeInBatches() {
    List<CodeRunRequest> batch = TestUtils.prepareCodeRequestBatch();

    codeRunService.batchRun(batch);

    verify(mockedCodeRunCache, times(3)).cache(any(), any());
  }

  @Test
  public void should_NotCacheCode_SecondTime() {
    CodeRunCache codeRunCacheSpy = spy(new RedisCodeRunCache());
    codeRunService = TestUtils.codeRunService(new Morrigan(), codeRunCacheSpy);
    CodeRunRequest request = TestUtils.codeRequest("abc");

    CodeRunResult result = codeRunService.run(request);
    codeRunService.run(request);

    verify(codeRunCacheSpy, times(1)).cache(eq(request), eq(result));
  }

  @Test
  public void should_GrabCodeRunResultFromCache_InsteadOfIntepreter() {
    CodeRunCache codeRunCacheSpy = spy(new RedisCodeRunCache());
    Morrigan morriganSpy = spy(new Morrigan());
    codeRunService = TestUtils.codeRunService(morriganSpy, codeRunCacheSpy);
    CodeRunRequest request = TestUtils.codeRequest("abc");

    CodeRunResult result = codeRunService.run(request);
    codeRunService.run(request);

    verify(codeRunCacheSpy, times(1)).cache(eq(request), eq(result));
    verify(codeRunCacheSpy, times(1)).get(request);
    verify(morriganSpy, times(1)).interpret(request.code());
  }

  @Test
  public void should_CacheInBatches() {
    List<CodeRunRequest> batch = List.of(TestUtils.codeRequest("1"), TestUtils.codeRequest("1"), TestUtils.codeRequest("1"));
    CodeRunCache codeRunCacheSpy = spy(new RedisCodeRunCache());
    Morrigan morriganSpy = spy(new Morrigan());
    codeRunService = TestUtils.codeRunService(morriganSpy, codeRunCacheSpy);

    codeRunService.batchRun(batch);

    verify(codeRunCacheSpy, times(2)).get(any());
    verify(codeRunCacheSpy, times(1)).cache(any(), any());
    verify(morriganSpy, times(1)).interpret(any());
  }
}
