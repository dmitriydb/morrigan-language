package ru.shanalotte.serviceregistry.mybatis.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import ru.shanalotte.serviceregistry.domain.MorriganPlatformService;

public interface ServiceMapper {

  @Update("UPDATE service SET active = false where id = #{id}")
  void setInactive(long id);

  @Select("INSERT INTO service (name, number, host) values (#{name}, #{number}, #{host}) returning id")
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
  })
  List<MorriganPlatformService> findActiveByName(String name);
}
