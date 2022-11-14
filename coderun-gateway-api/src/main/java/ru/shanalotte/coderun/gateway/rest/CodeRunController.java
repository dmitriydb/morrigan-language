package ru.shanalotte.coderun.gateway.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.shanalotte.coderun.api.CodeRunResult;
import ru.shanalotte.coderun.api.UserCodeRunRequest;
import ru.shanalotte.coderun.gateway.grpc.CodeRunRequestMessage;
import ru.shanalotte.coderun.gateway.grpc.CodeRunResultMessage;
import ru.shanalotte.coderun.gateway.grpc.CoderunBalancerGrpc;
import ru.shanalotte.coderun.gateway.service.ActiveCoderunLoadBalancers;
import ru.shanalotte.serviceregistry.api.KnownService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@Slf4j
public class CodeRunController {

  private final ActiveCoderunLoadBalancers activeCoderunLoadBalancers;

  private final ObjectMapper objectMapper = new ObjectMapper();

  public CodeRunController(ActiveCoderunLoadBalancers activeCoderunLoadBalancers) {
    this.activeCoderunLoadBalancers = activeCoderunLoadBalancers;
  }

  @PostMapping(value = "/run", produces = "application/json", consumes = "application/json")
  public ResponseEntity<CodeRunResult> run(@RequestBody String payload) {
    log.info("POST /run Received {}", payload);
    UserCodeRunRequest codeRunRequest = null;
    try {
      codeRunRequest = objectMapper.readValue(payload, UserCodeRunRequest.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return ResponseEntity.status(500).build();
    }
    List<KnownService> knownServices = new ArrayList<>(activeCoderunLoadBalancers.all());
    if (knownServices.size() == 0) {
      return ResponseEntity.status(500).build();
    }
    KnownService knownService = knownServices.get(ThreadLocalRandom.current().nextInt(knownServices.size()));
    log.info("Calling gRPC procedure at {}:{}", knownService.getHost(), knownService.getPort());
    ManagedChannel channel = ManagedChannelBuilder.forAddress(knownService.getHost(), knownService.getPort())
        .usePlaintext()
        .build();
    CoderunBalancerGrpc.CoderunBalancerBlockingStub stub
        = CoderunBalancerGrpc.newBlockingStub(channel);
    log.info("deserialized {}", codeRunRequest);
    CodeRunResultMessage result = stub.runCode(CodeRunRequestMessage.newBuilder()
        .setCode(codeRunRequest.code())
        .setLanguage(codeRunRequest.language().toString())
        .setUsername(codeRunRequest.username())
        .build());
    channel.shutdown();
    CodeRunResult codeRunResult = new CodeRunResult();
    codeRunResult.addToStderr(result.getStderr());
    codeRunResult.addToStdout(result.getStdout());
    return ResponseEntity.of(Optional.of(codeRunResult));
  }

}
