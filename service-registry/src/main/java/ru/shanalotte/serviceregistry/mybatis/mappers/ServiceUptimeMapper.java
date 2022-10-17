package ru.shanalotte.serviceregistry.mybatis.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import ru.shanalotte.serviceregistry.domain.MorriganPlatformServiceUptime;

public interface ServiceUptimeMapper {

  @Insert("INSERT INTO service_uptime (service_id) VALUES (#{id})")
  public void createUptime(long id);

  @Select("SELECT * FROM service_uptime WHERE service_id = #{id}")
  @Results(
      value = {
          @Result(property = "serviceId", column = "service_id"),
          @Result(property = "lag", column = "lag"),
          @Result(property = "uptime", column = "uptime"),
          @Result(property = "lastHeartbeatTs", column = "last_heartbeat_ts"),
      }
  )
  MorriganPlatformServiceUptime findById(long id);

  @Update("UPDATE service_uptime SET lag = #{lag}, uptime = #{uptime}, "
      + "last_heartbeat_ts=CURRENT_TIMESTAMP where service_id = #{id}")
  public void refreshUptime(long id, long lag, long uptime);
}
