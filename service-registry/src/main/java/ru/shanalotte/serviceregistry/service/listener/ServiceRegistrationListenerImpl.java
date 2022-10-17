package ru.shanalotte.serviceregistry.service.listener;

import org.springframework.stereotype.Service;
import ru.shanalotte.serviceregistry.dao.ServicesDao;
import ru.shanalotte.serviceregistry.dto.MorriganServiceRegistration;

@Service
public class ServiceRegistrationListenerImpl implements ServiceRegistrationListener {

  private ServicesDao servicesDao;

  public ServiceRegistrationListenerImpl(ServicesDao servicesDao) {
    this.servicesDao = servicesDao;
  }

  @Override
  public long processRegistration(MorriganServiceRegistration dto) {
    return servicesDao.create(dto);
  }

}
