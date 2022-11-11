package ru.shanalotte.coderun.gateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.shanalotte.serviceregistry.api.KnownService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
@Slf4j
public class ActiveCoderunLoadBalancers {

  @Value("${coderun.balancer.max.services.to.scan}")
  private int maxServicesToScan;

  @Value("${service.registry.url.pattern}")
  private String serviceRegistryUrlPattern;

  @Value("${coderun.loadbalancer.service.name}")
  private String serviceName;

  private final ServiceRegistryUrl serviceRegistryUrl;

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final Set<KnownService> knownServices = new CopyOnWriteArraySet<>();

  public ActiveCoderunLoadBalancers(ServiceRegistryUrl serviceRegistryUrl) {
    this.serviceRegistryUrl = serviceRegistryUrl;
  }

  @Scheduled(initialDelay = 0, fixedDelay = 10000)
  public void refresh() throws InterruptedException, JsonProcessingException {
    for (int i = 0; i < maxServicesToScan; i++) {
      String url = serviceRegistryUrl.prepare(serviceRegistryUrlPattern, i + 1, serviceName);
      HttpClient httpClient = HttpClient.newHttpClient();
      HttpRequest httpRequest = HttpRequest.newBuilder()
          .GET()
          .uri(URI.create(url))
          .build();
      HttpResponse<String> response = null;
      try {
        response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
      } catch (IOException e) {
        log.warn("Service {} is not responding", url);
        continue;
      }
      KnownService[] knownServicesConverted = objectMapper
          .readValue(response.body(), KnownService[].class);
      knownServices.addAll(Arrays.asList(knownServicesConverted));
      log.debug("Service registry {} returned {}", url,
          Arrays.toString(knownServicesConverted));
      log.info("Currently known services are: {}", knownServices);
    }
  }

  public Set<KnownService> all() {
    return knownServices;
  }

}
