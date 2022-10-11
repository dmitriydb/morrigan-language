package ru.shanalotte.serviceregistry.rest;

import java.util.List;
import java.util.stream.Collectors;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.shanalotte.serviceregistry.api.KnownService;
import ru.shanalotte.serviceregistry.dao.ServicesDAO;
import ru.shanalotte.serviceregistry.domain.MorriganPlatformService;

@RestController
@Tag(name = "Services", description = "Сервисы")
public class ServiceController {

  private ServicesDAO dao;

  public ServiceController(ServicesDAO dao) {
    this.dao = dao;
  }

  @GetMapping("/service")
  @Operation(summary = "Поиск активных сервисов по имени")
  public List<KnownService> getAllServices(  @Parameter(description = "Имя искомого сервиса") @RequestParam("name") String name) {
    List<MorriganPlatformService> activeServices = dao.findActiveServicesByName(name);
    return activeServices.stream().map(MorriganPlatformService::toKnownService).collect(Collectors.toList());
  }
}
