package ru.shanalotte.coderun.loadbalancer.service.scanner;

import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

public interface ServiceRegistryUrlPattern {

  public String pattern();

}
