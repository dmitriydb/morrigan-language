package ru.shanalotte.serviceregistry.dao;

import java.util.List;
import org.apache.ibatis.session.SqlSessionFactory;
import ru.shanalotte.serviceregistry.domain.MorriganPlatformService;
import ru.shanalotte.serviceregistry.domain.MorriganPlatformServiceUptime;
import ru.shanalotte.serviceregistry.dto.MorriganServiceRegistration;

public interface ServicesDao {

  long create(MorriganServiceRegistration dto);

  void abandon(long id);

  MorriganPlatformService findById(Long id);

  void refreshUptime(long id);

  List<MorriganPlatformService> findActiveServicesByName(String name);

  List<MorriganPlatformService> findAllActive();

  MorriganPlatformServiceUptime getUptime(long id);

  List<MorriganPlatformService> findAll();

  void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory);
}
