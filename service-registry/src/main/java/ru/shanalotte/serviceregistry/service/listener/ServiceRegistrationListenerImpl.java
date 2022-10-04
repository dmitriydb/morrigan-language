package ru.shanalotte.serviceregistry.service.listener;

import org.springframework.stereotype.Service;
import ru.shanalotte.serviceregistry.dao.ServicesDAO;
import ru.shanalotte.serviceregistry.dto.MorriganServiceRegistration;

@Service
public class ServiceRegistrationListenerImpl implements ServiceRegistrationListener {

  private ServicesDAO servicesDAO;

  public ServiceRegistrationListenerImpl(ServicesDAO servicesDAO) {
    this.servicesDAO = servicesDAO;
  }

  @Override
  public long processRegistration(MorriganServiceRegistration dto) {
    return servicesDAO.create(dto);
  }

}
