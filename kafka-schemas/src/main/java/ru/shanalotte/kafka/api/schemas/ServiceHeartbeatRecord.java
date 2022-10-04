package ru.shanalotte.kafka.api.schemas;

import java.util.UUID;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ServiceHeartbeatRecord {
  private String recordId = UUID.randomUUID().toString();
  private Long serviceId;
}
