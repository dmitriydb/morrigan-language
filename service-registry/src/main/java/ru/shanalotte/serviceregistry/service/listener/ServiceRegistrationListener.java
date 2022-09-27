package ru.shanalotte.serviceregistry.service.listener;

import ru.shanalotte.serviceregistry.dto.MorriganServiceHeartbeat;
import ru.shanalotte.serviceregistry.dto.MorriganServiceRegistration;

public interface ServiceRegistrationListener {
  void processRegistration(MorriganServiceRegistration dto);
  void processHeartbeat(MorriganServiceHeartbeat dto);
}
