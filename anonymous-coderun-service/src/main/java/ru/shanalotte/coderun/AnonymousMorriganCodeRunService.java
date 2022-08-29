package ru.shanalotte.coderun;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.shanalotte.Morrigan;
import ru.shanalotte.coderun.api.CodeRunRequest;

@RequiredArgsConstructor
public class AnonymousMorriganCodeRunService implements CodeRunService {

  private final @NonNull Morrigan morrigan;

  @Override
  public ru.shanalotte.coderun.CodeRunResult run(CodeRunRequest codeRunRequest) {
    CodeRunResult codeRunResult = new CodeRunResult();
    morrigan.interpret(codeRunRequest.code());
    morrigan.getInterpreter()
        .getResult()
        .forEach(codeRunResult::addToStdout);
    return codeRunResult;
  }
}
