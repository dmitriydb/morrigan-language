package ru.shanalotte.coderun.gateway.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("production")
public class ProductionServiceRegistryUrl implements ServiceRegistryUrl {
  @Override
  public String prepare(String pattern, int number, String serviceName) {
    return pattern.formatted(number, serviceName);
  }
}
