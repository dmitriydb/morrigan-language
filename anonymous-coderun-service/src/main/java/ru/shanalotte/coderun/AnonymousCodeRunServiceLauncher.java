package ru.shanalotte.coderun;


import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import ru.shanalotte.serviceregistry.client.Application;
import ru.shanalotte.serviceregistry.client.ServiceRegistryClient;

@Slf4j
public class AnonymousCodeRunServiceLauncher {

  public static void main(String[] args) {
    loadProperties();
    printProperties();
    new WebServer().start();
    ServiceRegistryClient serviceRegistryClient = Application.initializeContext(args).getBean(ServiceRegistryClient.class);
    serviceRegistryClient.startWorking("localhost", "coderun-service", (Integer) CommonProperties.property("server.port"));
  }

  private static void printProperties() {
    CommonProperties.printProperties();
  }

  private static void loadProperties() {
    log.info("Loading properties...");
    try {
      CommonProperties.loadProperties();
    } catch (IOException e) {
      log.warn("Couldn't load properties from application.properties, using default ones");
      e.printStackTrace();
    }
  }


}
