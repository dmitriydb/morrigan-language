package ru.shanalotte.serviceregistry.service.listener;

import org.springframework.stereotype.Service;
import ru.shanalotte.serviceregistry.dto.MorriganServiceRegistration;

@Service
public interface ServiceRegistrationListener {
  void processRegistration(MorriganServiceRegistration dto);
}
