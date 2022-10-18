package ru.shanalotte.serviceregistry.client;

public interface ServiceRegistrationService {
  void registerService(String host, String serviceName, int port);
}
