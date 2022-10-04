package ru.shanalotte.kafka.api.schemas;

import java.util.UUID;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ServiceLaunchRecord {
  private String recordId = UUID.randomUUID().toString();
  private String host;
  private int port;
  private String name;
  private String replyTo;
}
