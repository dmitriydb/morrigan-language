package ru.shanalotte.serviceregistry.mybatis.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import ru.shanalotte.serviceregistry.domain.MorriganPlatformService;

public interface ServiceMapper {

  @Update("UPDATE service SET abandon_ts = CURRENT_TIMESTAMP where id = #{id}")
  void setAbandonTs(long id);

  @Update("UPDATE service SET active = false where id = #{id}")
  void setInactive(long id);

  @Update("UPDATE service SET active = true where id = #{id}")
  void setActive(long id);

  @Select("INSERT INTO service (name, number, host, port) values (#{name}, #{number}, #{host}, #{port}) returning id")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  long addNewService(MorriganPlatformService service);

  @Select("SELECT * FROM service WHERE name = #{name} and active = true ORDER BY number DESC LIMIT 1")
  @Results(value = {
      @Result(property = "id", column = "id"),
      @Result(property = "name", column = "name"),
      @Result(property = "number", column = "number"),
      @Result(property = "host", column = "host"),
      @Result(property = "registrationTs", column = "registration_ts"),
      @Result(property = "abandonTs", column = "abandon_ts"),
      @Result(property = "isActive", column = "active"),
      @Result(property = "port", column = "port")
  })
  MorriganPlatformService findMaxNumberByName(String name);

  @Select("SELECT * FROM service WHERE id = #{id}")
  @Results(value = {
      @Result(property = "id", column = "id"),
      @Result(property = "name", column = "name"),
      @Result(property = "number", column = "number"),
      @Result(property = "host", column = "host"),
      @Result(property = "registrationTs", column = "registration_ts"),
      @Result(property = "abandonTs", column = "abandon_ts"),
      @Result(property = "isActive", column = "active"),
      @Result(property = "port", column = "port")
  })
  MorriganPlatformService findById(long id);

  @Select("SELECT * FROM service WHERE name = #{name} and active = true order by number asc")
  @Results(value = {
      @Result(property = "id", column = "id"),
      @Result(property = "name", column = "name"),
      @Result(property = "number", column = "number"),
      @Result(property = "host", column = "host"),
      @Result(property = "registrationTs", column = "registration_ts"),
      @Result(property = "abandonTs", column = "abandon_ts"),
      @Result(property = "isActive", column = "active"),
      @Result(property = "port", column = "port")
  })
  List<MorriganPlatformService> findActiveByName(String name);

  @Select("SELECT * FROM service WHERE active = true")
  @Results(value = {
      @Result(property = "id", column = "id"),
      @Result(property = "name", column = "name"),
      @Result(property = "number", column = "number"),
      @Result(property = "host", column = "host"),
      @Result(property = "registrationTs", column = "registration_ts"),
      @Result(property = "abandonTs", column = "abandon_ts"),
      @Result(property = "isActive", column = "active"),
      @Result(property = "port", column = "port")
  })
  List<MorriganPlatformService> findAllActive();

  @Select("SELECT * FROM service")
  @Results(value = {
      @Result(property = "id", column = "id"),
      @Result(property = "name", column = "name"),
      @Result(property = "number", column = "number"),
      @Result(property = "host", column = "host"),
      @Result(property = "registrationTs", column = "registration_ts"),
      @Result(property = "abandonTs", column = "abandon_ts"),
      @Result(property = "isActive", column = "active"),
      @Result(property = "port", column = "port")
  })
  List<MorriganPlatformService> findAll();

}
