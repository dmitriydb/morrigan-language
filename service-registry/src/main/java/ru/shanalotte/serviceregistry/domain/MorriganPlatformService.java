package ru.shanalotte.serviceregistry.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.shanalotte.serviceregistry.api.KnownService;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MorriganPlatformService {

  private Long id;
  private String name;
  private int number;
  private int port;
  private String host;
  private LocalDateTime registrationTs;
  private LocalDateTime abandonTs;
  private boolean isActive;

  public KnownService toKnownService() {
    return new KnownService(name, port, host);
  }

}
