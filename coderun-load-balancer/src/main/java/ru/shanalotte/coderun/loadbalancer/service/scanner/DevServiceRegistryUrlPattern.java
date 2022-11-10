package ru.shanalotte.coderun.loadbalancer.service.scanner;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class DevServiceRegistryUrlPattern implements ServiceRegistryUrlPattern {

  @Override
  public String pattern() {
    return "http://localhost:9002/service?name=anonymous-coderun-service";
  }
}
