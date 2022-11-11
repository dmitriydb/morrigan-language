package ru.shanalotte.coderun.loadbalancer;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.shanalotte.coderun.loadbalancer.service.ServiceRegistryUrl;

@Service
@Profile("production")
public class ProductionServiceRegistryUrl implements ServiceRegistryUrl {
  @Override
  public String prepare(String pattern, int number, String serviceName) {
    return pattern.formatted(number, serviceName);
  }
}
