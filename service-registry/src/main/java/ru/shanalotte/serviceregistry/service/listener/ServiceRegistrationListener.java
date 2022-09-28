package ru.shanalotte.serviceregistry.service.listener;

import ru.shanalotte.serviceregistry.dto.MorriganServiceRegistration;

public interface ServiceRegistrationListener {
  void processRegistration(MorriganServiceRegistration dto);
}
