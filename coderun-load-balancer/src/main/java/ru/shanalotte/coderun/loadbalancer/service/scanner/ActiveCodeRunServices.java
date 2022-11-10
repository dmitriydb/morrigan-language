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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.shanalotte.serviceregistry.api.KnownService;

@Service
public class ActiveCodeRunServices {

  @Value("${coderun.balancer.max.services.to.scan}")
  private int maxServicesToScan;

  private final ServiceRegistryUrlPattern serviceRegistryUrlPattern;

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final Set<KnownService> knownServices = new CopyOnWriteArraySet<>();

  public ActiveCodeRunServices(ServiceRegistryUrlPattern serviceRegistryUrlPattern) {
    this.serviceRegistryUrlPattern = serviceRegistryUrlPattern;
  }

  @Scheduled(initialDelay = 0, fixedDelay = 1000)
  public void refresh() throws InterruptedException, JsonProcessingException {
    for (int i = 0; i < maxServicesToScan; i++) {
      String serviceRegistryUrl = serviceRegistryUrlPattern.pattern().formatted(i + 1);
      HttpClient httpClient = HttpClient.newHttpClient();
      HttpRequest httpRequest = HttpRequest.newBuilder()
          .GET()
          .uri(URI.create(serviceRegistryUrl))
          .build();
      HttpResponse<String> response = null;
      try {
        response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
      } catch (IOException e) {
        System.out.printf("service-registry #%d is not responding %n", i + 1);
        continue;
      }
      KnownService[] knownServicesConverted = objectMapper.readValue(response.body(), KnownService[].class);
      knownServices.addAll(Arrays.asList(knownServicesConverted));
    }
  }

  public Set<KnownService> all() {
    return knownServices;
  }

}
