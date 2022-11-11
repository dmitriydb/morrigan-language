package ru.shanalotte.coderun.loadbalancer.grpc;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.shanalotte.coderun.api.SupportedLanguage;
import ru.shanalotte.coderun.api.UserCodeRunRequest;
import ru.shanalotte.coderun.loadbalancer.service.runner.CodeRunRequestRunner;

import java.io.IOException;

@Service
@Slf4j
public class CoderunServiceImpl extends CoderunBalancerGrpc.CoderunBalancerImplBase {

  @Autowired
  private CodeRunRequestRunner codeRunRequestRunner;

  @Override
  public void runCode(CodeRunRequest request, StreamObserver<CodeRunResult> responseObserver) {
    log.info("Incoming request {} {} {}", request.getCode(), request.getLanguage(), request.getUsername());
    ru.shanalotte.coderun.api.CodeRunRequest userRequest =
        new UserCodeRunRequest(SupportedLanguage.valueOf(request.getLanguage().toUpperCase()), request.getCode(), request.getUsername());
    try {
      ru.shanalotte.coderun.api.CodeRunResult result = codeRunRequestRunner.run(userRequest);
      CodeRunResult codeRunResult = CodeRunResult.newBuilder()
          .setStdout(result.getStdout().toString())
          .setStderr(result.getStderr().toString())
          .build();

      responseObserver.onNext(codeRunResult);
      responseObserver.onCompleted();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
