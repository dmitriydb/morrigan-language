package ru.shanalotte.serviceregistry.client;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.PostConstruct;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

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
