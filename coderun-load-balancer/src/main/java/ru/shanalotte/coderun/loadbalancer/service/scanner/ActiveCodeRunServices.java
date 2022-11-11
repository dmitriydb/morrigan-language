package ru.shanalotte.coderun.loadbalancer.service.scanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.shanalotte.serviceregistry.api.KnownService;

@Service
@Slf4j
public class ActiveCodeRunServices {

  @Value("${coderun.balancer.max.services.to.scan}")
  private int maxServicesToScan;

  @Value("${service.registry.url.pattern}")
  private String serviceRegistryUrlPattern;

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final Set<KnownService> knownServices = new CopyOnWriteArraySet<>();



  @Scheduled(initialDelay = 0, fixedDelay = 10000)
  public void refresh() throws InterruptedException, JsonProcessingException {
    for (int i = 0; i < maxServicesToScan; i++) {
      String serviceRegistryUrl = serviceRegistryUrlPattern.formatted(i + 1);
      HttpClient httpClient = HttpClient.newHttpClient();
      HttpRequest httpRequest = HttpRequest.newBuilder()
          .GET()
          .uri(URI.create(serviceRegistryUrl))
          .build();
      HttpResponse<String> response = null;
      try {
        response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
      } catch (IOException e) {
        log.warn("Service {} is not responding", serviceRegistryUrl);
        continue;
      }
      KnownService[] knownServicesConverted = objectMapper
          .readValue(response.body(), KnownService[].class);
      knownServices.addAll(Arrays.asList(knownServicesConverted));
      log.debug("Service registry {} returned {}", serviceRegistryUrl,
          Arrays.toString(knownServicesConverted));
      log.info("Currently known services are: {}", knownServices);
    }
  }

  public Set<KnownService> all() {
    return knownServices;
  }

}
