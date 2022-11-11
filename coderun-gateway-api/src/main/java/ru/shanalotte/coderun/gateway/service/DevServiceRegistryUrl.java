package ru.shanalotte.coderun.gateway.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class DevServiceRegistryUrl implements ServiceRegistryUrl {
  @Override
  public String prepare(String pattern, int number, String serviceName) {
    return pattern.formatted(serviceName);
  }
}
