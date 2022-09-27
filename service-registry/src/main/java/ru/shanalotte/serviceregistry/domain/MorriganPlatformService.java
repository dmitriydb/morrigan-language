package ru.shanalotte.serviceregistry.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Builder
public class MorriganPlatformService {

  private Long id;
  private String name;
  private int number;
  private String host;
  private LocalDateTime registrationTs;
  private LocalDateTime abandonTs;

}
