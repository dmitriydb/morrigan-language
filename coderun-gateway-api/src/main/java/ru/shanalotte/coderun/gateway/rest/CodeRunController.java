package ru.shanalotte.coderun.gateway.rest;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shanalotte.coderun.gateway.grpc.CodeRunRequest;
import ru.shanalotte.coderun.gateway.grpc.CodeRunResult;
import ru.shanalotte.coderun.gateway.grpc.CoderunBalancerGrpc;
import ru.shanalotte.coderun.gateway.service.ActiveCoderunLoadBalancers;
import ru.shanalotte.serviceregistry.api.KnownService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class CodeRunController {

  private final ActiveCoderunLoadBalancers activeCoderunLoadBalancers;
  @Value("${coderun.loadbalancer.url.pattern}")
  private String codeRunBalancerUrlPattern;
  @Value("${coderun.loadbalancer.port}")
  private int codeRunBalancerPort;

  public CodeRunController(ActiveCoderunLoadBalancers activeCoderunLoadBalancers) {
    this.activeCoderunLoadBalancers = activeCoderunLoadBalancers;
  }

  @PostMapping("/run")
  public @ResponseBody
  ResponseEntity<CodeRunResult> run(@RequestBody CodeRunRequest codeRunRequest) {
    List<KnownService> knownServices = new ArrayList<>(activeCoderunLoadBalancers.all());
    if (knownServices.size() == 0) {
      return ResponseEntity.status(500).build();
    }
    KnownService knownService = knownServices.get(ThreadLocalRandom.current().nextInt());
    ManagedChannel channel = ManagedChannelBuilder.forAddress(knownService.getHost(), knownService.getPort())
        .usePlaintext()
        .build();
    CoderunBalancerGrpc.CoderunBalancerBlockingStub stub
        = CoderunBalancerGrpc.newBlockingStub(channel);
    CodeRunResult result = stub.runCode(CodeRunRequest.newBuilder()
        .setCode(codeRunRequest.getCode())
        .setLanguage(codeRunRequest.getLanguage())
        .build());
    channel.shutdown();
    return ResponseEntity.of(Optional.of(result));
  }

}
