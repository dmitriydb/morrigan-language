package ru.shanalotte.serviceregistry.dto;

import java.time.LocalDateTime;
import lombok.ToString;
import lombok.Value;

@Value
@ToString
public class MorriganServiceHeartbeat {
  Long id;
  LocalDateTime heartbeatTs;
}
