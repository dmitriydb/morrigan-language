package ru.shanalotte.serviceregistry.dao;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.shanalotte.serviceregistry.domain.MorriganPlatformService;
import ru.shanalotte.serviceregistry.domain.MorriganPlatformServiceUptime;
import ru.shanalotte.serviceregistry.dto.MorriganServiceHeartbeat;
import ru.shanalotte.serviceregistry.dto.MorriganServiceRegistration;

public interface ServicesDAO {
  long create(MorriganServiceRegistration dto);
  void abandon(long id);
  MorriganPlatformService findById(Long id);
  void refreshUptime(long id);
  List<MorriganPlatformService> findActiveServicesByName(String name);
  MorriganPlatformServiceUptime getUptime(long id);
}
