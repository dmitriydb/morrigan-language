package ru.shanalotte.coderun.loadbalancer.service;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.shanalotte.coderun.api.AnonymousCodeRunRequest;
import ru.shanalotte.coderun.api.CodeRunRequest;
import ru.shanalotte.coderun.api.SupportedLanguage;
import ru.shanalotte.coderun.loadbalancer.service.runner.CodeRunRequestRunner;

@SpringBootTest
public class CodeRunRequestRunnerTest {

  @Autowired
  CodeRunRequestRunner codeRunRequestRunner;

  @Test
  public void should_Work() throws IOException, InterruptedException {
    CodeRunRequest codeRunRequest = new AnonymousCodeRunRequest(SupportedLanguage.MORRIGAN, "morrigan remembers what is 5.");
    codeRunRequestRunner.run(codeRunRequest);
  }


}