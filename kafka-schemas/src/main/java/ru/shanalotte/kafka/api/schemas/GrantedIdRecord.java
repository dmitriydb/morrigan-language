package ru.shanalotte.kafka.api.schemas;

import java.util.UUID;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GrantedIdRecord {
  private String recordId = UUID.randomUUID().toString();
  private String originalRecordId;
  private Long grantedId;
}
