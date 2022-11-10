package ru.shanalotte.serviceregistry.client;

import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.stereotype.Component;

@Component
public class SpringBootBasedServiceRegistryClient implements ServiceRegistryClient {

  private final AtomicBoolean isStarted = new AtomicBoolean(false);
  private final ServiceRegistrationService serviceRegistrationService;

  public SpringBootBasedServiceRegistryClient(ServiceRegistrationService
                                                  serviceRegistrationService) {
    this.serviceRegistrationService = serviceRegistrationService;
  }

  @Override
  public void startWorking(String host, String serviceName, int port) {
    serviceRegistrationService.registerService(host, serviceName, port);
  }
}
