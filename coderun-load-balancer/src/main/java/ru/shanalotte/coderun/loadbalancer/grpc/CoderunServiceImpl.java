package ru.shanalotte.coderun.loadbalancer.grpc;

import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.shanalotte.coderun.api.SupportedLanguage;
import ru.shanalotte.coderun.api.UserCodeRunRequest;
import ru.shanalotte.coderun.loadbalancer.service.runner.CodeRunRequestRunner;

import java.io.IOException;

@Service
public class CoderunServiceImpl extends CoderunBalancerGrpc.CoderunBalancerImplBase {

  @Autowired
  private CodeRunRequestRunner codeRunRequestRunner;

  @Override
  public void runCode(CodeRunRequest request, StreamObserver<CodeRunResult> responseObserver) {
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
