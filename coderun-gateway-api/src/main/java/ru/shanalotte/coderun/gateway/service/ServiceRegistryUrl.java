package ru.shanalotte.coderun.gateway.service;

public interface ServiceRegistryUrl {
  public String prepare(String pattern, int number, String serviceName);
}
