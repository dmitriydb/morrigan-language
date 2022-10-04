package ru.shanalotte.serviceregistry.service.listener;

import org.springframework.stereotype.Service;
import ru.shanalotte.serviceregistry.dao.ServicesDAO;
import ru.shanalotte.serviceregistry.dto.MorriganServiceHeartbeat;

@Service
public class ServiceHeartbeatListenerImpl implements ServiceHeartbeatListener {

  private ServicesDAO servicesDAO;

  public ServiceHeartbeatListenerImpl(ServicesDAO servicesDAO) {
    this.servicesDAO = servicesDAO;
  }

  @Override
  public void processHeartbeat(MorriganServiceHeartbeat dto) {
    servicesDAO.refreshUptime(dto.getId());
  }
}
