package ru.shanalotte.serviceregistry.jmx;

import java.util.List;

public interface StatsMBean {
  List<String> activeServices();
  int activeServicesQty();
}
