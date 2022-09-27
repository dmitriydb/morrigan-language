package ru.shanalotte.serviceregistry.mybatis.mappers;

import org.apache.ibatis.annotations.Insert;
import ru.shanalotte.serviceregistry.domain.MorriganPlatformService;

public interface ServiceMapper {

  @Insert("INSERT INTO service (name, number, host) values (#{name}, #{number}, #{host})")
  void addNewService(MorriganPlatformService service);

}
