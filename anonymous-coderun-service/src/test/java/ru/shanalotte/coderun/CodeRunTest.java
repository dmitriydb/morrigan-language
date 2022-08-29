package ru.shanalotte.coderun;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import ru.shanalotte.Morrigan;
import ru.shanalotte.coderun.api.CodeRunRequest;
import ru.shanalotte.coderun.api.SimpleCodeRunRequest;
import ru.shanalotte.coderun.api.SupportedLanguage;

public class CodeRunTest {

  @Test
  public void should_RunSomething() {
    CodeRunRequest codeRunRequest = SimpleCodeRunRequest.builder()
        .language(SupportedLanguage.MORRIGAN)
        .code("""
            morrigan says that a is 10.
            morrigan remembers what is a.
            """)
        .build();
    CodeRunService codeRunService = new AnonymousMorriganCodeRunService(new Morrigan());
    CodeRunResult codeRunResult = codeRunService.run(codeRunRequest);
    assertThat(codeRunResult.stdout().contains("10")).isTrue();
  }

  @Test
  public void should_RunSomethingElse() {
    CodeRunRequest codeRunRequest = SimpleCodeRunRequest.builder()
        .language(SupportedLanguage.MORRIGAN)
        .code("""
            morrigan remembers what is [Hello, world.].
            """)
        .build();
    CodeRunService codeRunService = new AnonymousMorriganCodeRunService(new Morrigan());
    CodeRunResult codeRunResult = codeRunService.run(codeRunRequest);
    assertThat(codeRunResult.stdout().contains("Hello, world.")).isTrue();
  }
}
