package ru.shanalotte.serviceregistry.dao;

import ru.shanalotte.serviceregistry.domain.MorriganPlatformService;
import ru.shanalotte.serviceregistry.dto.MorriganServiceHeartbeat;
import ru.shanalotte.serviceregistry.dto.MorriganServiceRegistration;

public interface ServicesDAO {
  MorriganPlatformService create(MorriganServiceRegistration dto);
  void abandon(MorriganPlatformService service);
  MorriganPlatformService findById(Long id);
  void updateUptime(MorriganServiceHeartbeat dto);
}
