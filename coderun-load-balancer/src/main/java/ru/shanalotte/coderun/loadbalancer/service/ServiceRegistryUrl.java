package ru.shanalotte.coderun.loadbalancer.service;

public interface ServiceRegistryUrl {
  public String prepare(String pattern, int number, String serviceName);
}
