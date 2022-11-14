package ru.shanalotte.coderun.gateway.rest;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.shanalotte.coderun.api.CodeRunRequest;
import ru.shanalotte.coderun.api.CodeRunResult;
import ru.shanalotte.coderun.gateway.grpc.CodeRunRequestMessage;
import ru.shanalotte.coderun.gateway.grpc.CodeRunResultMessage;
import ru.shanalotte.coderun.gateway.grpc.CoderunBalancerGrpc;

@SpringBootTest
class CodeRunControllerTest {

  @Test
  public void abc() {
    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8866)
        .usePlaintext()
        .build();
    CoderunBalancerGrpc.CoderunBalancerBlockingStub stub
        = CoderunBalancerGrpc.newBlockingStub(channel);
    CodeRunResultMessage result = stub.runCode(CodeRunRequestMessage.newBuilder()
        .setCode("morrigan remembers what is 5.")
        .setLanguage("MORRIGAN")
        .setUsername("oleg")
        .build());
    System.out.println(result);
    channel.shutdown();
  }

}