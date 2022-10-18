package ru.shanalotte.serviceregistry.client;

public class Launcher {

  public static void main(String[] args) {
    ServiceRegistryClient serviceRegistryClient = new SpringBootBasedServiceRegistryClient();
    serviceRegistryClient.startWorking("a", "b", 33);
  }
}
