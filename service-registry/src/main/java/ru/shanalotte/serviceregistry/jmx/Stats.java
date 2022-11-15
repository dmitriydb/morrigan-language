package ru.shanalotte.serviceregistry.jmx;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.stereotype.Service;
import ru.shanalotte.serviceregistry.dao.ServicesDao;

import javax.annotation.ManagedBean;
import java.util.List;
import java.util.stream.Collectors;

@Service
@ManagedBean
public class Stats implements StatsMBean {

  private final ServicesDao servicesDao;

  public Stats(ServicesDao servicesDao) {
    this.servicesDao = servicesDao;
  }

  @ManagedOperation
  @Override
  public List<String> activeServices() {
    return servicesDao.findAllActive()
        .stream()
        .map(service -> service.getNumber() + "." + service.getName() + "=" + service.getHost() + ":" + service.getPort())
        .collect(Collectors.toList());
  }

  @ManagedAttribute
  @Override
  public int activeServicesQty() {
    return servicesDao.findAllActive().size();
  }
}
