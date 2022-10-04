package ru.shanalotte.serviceregistry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import ru.shanalotte.serviceregistry.service.listener.ServiceHeartbeatListener;
import ru.shanalotte.serviceregistry.service.monitoring.InactiveServicesMonitoringService;

@SpringBootApplication
@ComponentScan("ru.shanalotte.serviceregistry")
public class ServiceRegistryLauncher implements CommandLineRunner {

  private InactiveServicesMonitoringService inactiveServicesMonitoringService;

  public ServiceRegistryLauncher(InactiveServicesMonitoringService inactiveServicesMonitoringService) {
    this.inactiveServicesMonitoringService = inactiveServicesMonitoringService;
  }

  public static void main(String[] args) {
    SpringApplication.run(ServiceRegistryLauncher.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    inactiveServicesMonitoringService.startMonitoring();
  }
}
