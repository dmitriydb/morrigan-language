package ru.shanalotte.serviceregistry.client;

import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("ru.shanalotte.serviceregistry.client")
public class SpringBootBasedServiceRegistryClient implements ServiceRegistryClient {

  private final AtomicBoolean isStarted = new AtomicBoolean(false);

  @Override
  public void startWorking(String host, String serviceName, int port) {
    if (!isStarted.get()) {
      System.out.println("STARTING CONTEXT");
      SpringApplication.run(SpringBootBasedServiceRegistryClient.class);
      isStarted.set(true);
    }
    ServiceRegistrationService service = SpringContext.getBean(ServiceRegistrationService.class);
    service.registerService(host, serviceName, port);
  }
}
