package ru.shanalotte.serviceregistry.service.listener;

import org.springframework.stereotype.Service;
import ru.shanalotte.serviceregistry.dto.MorriganServiceHeartbeat;

@Service
public interface ServiceHeartbeatListener {
  void processHeartbeat(MorriganServiceHeartbeat dto);
}
