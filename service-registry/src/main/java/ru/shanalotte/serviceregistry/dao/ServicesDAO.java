package ru.shanalotte.serviceregistry.dao;

import java.util.List;
import ru.shanalotte.serviceregistry.domain.MorriganPlatformService;
import ru.shanalotte.serviceregistry.domain.MorriganPlatformServiceUptime;
import ru.shanalotte.serviceregistry.dto.MorriganServiceRegistration;

public interface ServicesDAO {

  long create(MorriganServiceRegistration dto);

  void abandon(long id);

  MorriganPlatformService findById(Long id);

  void refreshUptime(long id);

  List<MorriganPlatformService> findActiveServicesByName(String name);

  List<MorriganPlatformService> findAllActive();

  MorriganPlatformServiceUptime getUptime(long id);


}
