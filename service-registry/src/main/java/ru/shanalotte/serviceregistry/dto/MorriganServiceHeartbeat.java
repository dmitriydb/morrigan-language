package ru.shanalotte.serviceregistry.dto;

import java.time.LocalDateTime;
import lombok.ToString;
import lombok.Value;
import ru.shanalotte.kafka.api.schemas.ServiceHeartbeatRecord;

@Value
@ToString
public class MorriganServiceHeartbeat {
  Long id;
  LocalDateTime heartbeatTs;

  public static MorriganServiceHeartbeat of(ServiceHeartbeatRecord dto) {
    return new MorriganServiceHeartbeat(dto.getServiceId(), LocalDateTime.now());
  }
}
