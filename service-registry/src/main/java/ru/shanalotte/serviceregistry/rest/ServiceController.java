package ru.shanalotte.serviceregistry.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.shanalotte.serviceregistry.api.KnownService;
import ru.shanalotte.serviceregistry.dao.ServicesDao;
import ru.shanalotte.serviceregistry.domain.MorriganPlatformService;

@RestController
@Tag(name = "Services", description = "Сервисы")
public class ServiceController {

  private ServicesDao dao;

  public ServiceController(ServicesDao dao) {
    this.dao = dao;
  }

  @GetMapping("/service")
  @Operation(summary = "Поиск активных сервисов по имени")
  public List<KnownService> getServicesByName(
      @Parameter(description = "Имя искомого сервиса") @RequestParam("name") String name) {
    List<MorriganPlatformService> activeServices = dao.findActiveServicesByName(name);
    return activeServices
        .stream()
        .map(MorriganPlatformService::toKnownService).collect(Collectors.toList());
  }

  @GetMapping("/service/active")
  @Operation(summary = "Получение списка всех активных сервисов")
  public List<KnownService> getAllServices() {
    List<MorriganPlatformService> activeServices = dao.findAllActive();
    return activeServices
        .stream()
        .map(MorriganPlatformService::toKnownService).collect(Collectors.toList());
  }

}
