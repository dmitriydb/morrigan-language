package ru.shanalotte.coderun.gateway;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ru.shanalotte.coderun.gateway.grpc.CodeRunRequest;
import ru.shanalotte.coderun.gateway.grpc.CodeRunResult;
import ru.shanalotte.coderun.gateway.grpc.CoderunBalancerGrpc;

public class CodeGatewayApiLauncher {

  public static void main(String[] args) {
    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8866)
        .usePlaintext()
        .build();

    CoderunBalancerGrpc.CoderunBalancerBlockingStub stub
        = CoderunBalancerGrpc.newBlockingStub(channel);

    CodeRunResult result = stub.runCode(CodeRunRequest.newBuilder()
        .setCode("morrigan remembers what is 5.")
        .setLanguage("morrigan")
        .build());

    System.out.println(result);
    channel.shutdown();
  }
}
