package ru.shanalotte.coderun.loadbalancer.service.runner;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.shanalotte.coderun.api.CodeRunRequest;
import ru.shanalotte.coderun.api.CodeRunResult;
import ru.shanalotte.coderun.loadbalancer.service.scanner.ActiveCodeRunServices;
import ru.shanalotte.serviceregistry.api.KnownService;

@Service
@Slf4j
public class CodeRunRequestRunner {

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final ActiveCodeRunServices activeCodeRunServices;

  public CodeRunRequestRunner(ActiveCodeRunServices activeCodeRunServices) {
    this.activeCodeRunServices = activeCodeRunServices;
  }

  public CodeRunResult run(CodeRunRequest request) throws IOException, InterruptedException {
    log.info("Received code run request {}", request);
    List<KnownService> knownServices = new ArrayList<>(activeCodeRunServices.all());
    log.debug("At this moment {} services is known.", knownServices);
    KnownService randomService = knownServices
        .get(ThreadLocalRandom.current().nextInt(knownServices.size()));
    String url = buildServiceUrl(knownServices.iterator().next());
    log.info("Chosen service for run: {}", url);
    HttpClient httpClient = HttpClient.newHttpClient();
    String payload = objectMapper.writeValueAsString(request);
    HttpRequest httpRequest = HttpRequest
        .newBuilder()
        .POST(HttpRequest.BodyPublishers.ofString(payload))
        .uri(URI.create(url))
        .build();
    HttpResponse<String> response = httpClient
        .send(httpRequest, HttpResponse.BodyHandlers.ofString());
    log.info("Received response: {}", response.body());
    return objectMapper.readValue(response.body(), CodeRunResult.class);
  }

  private String buildServiceUrl(KnownService knownService) {
    return "http://%s:%d/run".formatted(knownService.getHost(), knownService.getPort());
  }
}
