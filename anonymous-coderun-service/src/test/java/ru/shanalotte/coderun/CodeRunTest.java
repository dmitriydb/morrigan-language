package ru.shanalotte.coderun;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import ru.shanalotte.coderun.api.CodeRunRequest;
import ru.shanalotte.coderun.api.AnonymousCodeRunRequest;
import ru.shanalotte.coderun.api.SupportedLanguage;

public class CodeRunTest {

  @Test
  public void should_RunSomething() {
    CodeRunRequest codeRunRequest = AnonymousCodeRunRequest.builder()
        .language(SupportedLanguage.MORRIGAN)
        .code("""
            morrigan says that a is 10.
            morrigan remembers what is a.
            """)
        .build();
    CodeRunService codeRunService = TestUtils.codeRunServiceWithMockedCache();

    CodeRunResult codeRunResult = codeRunService.run(codeRunRequest);

    assertThat(codeRunResult.stdout().contains("10.0")).isTrue();
  }

  @Test
  public void should_RunSomethingElse() {
    CodeRunRequest codeRunRequest = AnonymousCodeRunRequest.builder()
        .language(SupportedLanguage.MORRIGAN)
        .code("""
            morrigan remembers what is [Hello, world.].
            """)
        .build();
    CodeRunService codeRunService = TestUtils.codeRunServiceWithMockedCache();

    CodeRunResult codeRunResult = codeRunService.run(codeRunRequest);

    assertThat(codeRunResult.stdout().contains("Hello, world.")).isTrue();
  }

  @Test
  public void should_RunCodeInBatches() {
    List<CodeRunRequest> batch = TestUtils.prepareCodeRequestBatch();
    CodeRunService codeRunService = TestUtils.codeRunServiceWithMockedCache();

    List<CodeRunResult> result = codeRunService.batchRun(batch);
    System.out.println(result);
    assertThat(result.get(0).stdout().get(0)).isEqualTo("1.0");
    assertThat(result.get(1).stdout().get(0)).isEqualTo("2.0");
    assertThat(result.get(2).stdout().get(0)).isEqualTo("3.0");
  }

  @Test
  public void stdErr_shouldBeEmpty_WhenCodeIsValid() {
    CodeRunService codeRunService = TestUtils.codeRunServiceWithMockedCache();
    CodeRunResult codeRunResult = codeRunService.run(TestUtils.randomCodeRequest());
    assertThat(codeRunResult.stderr()).isEmpty();
    assertThat(codeRunResult.stdout()).isNotEmpty();
  }

  @Test
  public void stdErr_shouldBeNotEmpty_WhenCodeIsInvalid() {
    CodeRunService codeRunService = TestUtils.codeRunServiceWithMockedCache();
    CodeRunResult codeRunResult = codeRunService.run(AnonymousCodeRunRequest.builder()
        .code(".")
        .language(SupportedLanguage.MORRIGAN)
        .build());
    assertThat(codeRunResult.stdout()).isEmpty();
    assertThat(codeRunResult.stderr()).isNotEmpty();
  }
}
