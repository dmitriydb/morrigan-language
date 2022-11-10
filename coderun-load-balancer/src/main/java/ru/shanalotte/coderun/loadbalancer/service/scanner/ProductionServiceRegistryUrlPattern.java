package ru.shanalotte.coderun.loadbalancer.service.scanner;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("production")
public class ProductionServiceRegistryUrlPattern implements ServiceRegistryUrlPattern {

  @Override
  public String pattern() {
    return "http://service-registry-%d.morrigan:9004/service?name=anonymous-coderun-service";
  }
}
