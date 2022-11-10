package ru.shanalotte.it;

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
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.shanalotte.Morrigan;
import ru.shanalotte.coderun.api.CodeRunResult;
import ru.shanalotte.coderun.CodeRunService;
import ru.shanalotte.coderun.TestUtils;
import ru.shanalotte.coderun.api.CodeRunRequest;
import ru.shanalotte.coderun.cache.CodeRunCache;
import ru.shanalotte.coderun.cache.RedisCodeRunCache;

@Testcontainers
public class CachingIT {

  @Container
  public GenericContainer redis = new GenericContainer(DockerImageName.parse("redis:5.0.3-alpine"))
      .withExposedPorts(6379);

  private CodeRunCache mockedCodeRunCache = Mockito.mock(CodeRunCache.class);
  private CodeRunService codeRunService = TestUtils.codeRunService(mockedCodeRunCache);
  private CodeRunCache codeRunCacheSpy;

  @BeforeEach
  public void setUp() {
    mockedCodeRunCache = Mockito.mock(CodeRunCache.class);
    codeRunService = TestUtils.codeRunService(mockedCodeRunCache);
    codeRunCacheSpy = spy(new RedisCodeRunCache(redis.getHost(), redis.getFirstMappedPort()));
  }

  @Test
  public void should_CacheCodeRun() {
    CodeRunRequest request = TestUtils.randomCodeRequest();
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
    codeRunService = TestUtils.codeRunService(codeRunCacheSpy);
    CodeRunRequest request = TestUtils.randomCodeRequest();

    CodeRunResult result = codeRunService.run(request);
    codeRunService.run(request);

    verify(codeRunCacheSpy, times(1)).cache(eq(request), eq(result));
  }

  @Test
  public void should_GrabCodeRunResultFromCache_InsteadOfIntepreter() {
    Morrigan morriganSpy = spy(new Morrigan());
    codeRunService = TestUtils.codeRunService(morriganSpy, codeRunCacheSpy);
    CodeRunRequest request = TestUtils.randomCodeRequest();

    CodeRunResult result = codeRunService.run(request);
    codeRunService.run(request);

    verify(codeRunCacheSpy, times(1)).cache(eq(request), eq(result));
    verify(codeRunCacheSpy, times(1)).get(request);
    verify(morriganSpy, times(1)).interpret(request.code());
  }

  @Test
  public void should_CacheInBatches() {
    CodeRunRequest request = TestUtils.randomCodeRequest();
    List<CodeRunRequest> batch = List.of(request, request, request);
    Morrigan morriganSpy = spy(new Morrigan());
    codeRunService = TestUtils.codeRunService(morriganSpy, codeRunCacheSpy);

    codeRunService.batchRun(batch);

    verify(codeRunCacheSpy, times(2)).get(any());
    verify(codeRunCacheSpy, times(1)).cache(any(), any());
    verify(morriganSpy, times(1)).interpret(any());
  }

}
