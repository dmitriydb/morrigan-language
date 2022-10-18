package ru.shanalotte.serviceregistry.client;

public interface ServiceRegistryClient {
  void startWorking(String host, String serviceName, int port);
}
