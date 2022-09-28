package ru.shanalotte.serviceregistry.domain;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MorriganPlatformServiceUptime {

  private Long serviceId;
  private LocalDateTime lastHeartbeatTs;
  private long lag;
  private long uptime;

}
