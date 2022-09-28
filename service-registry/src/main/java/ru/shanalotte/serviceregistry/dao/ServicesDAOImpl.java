package ru.shanalotte.serviceregistry.dao;

import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ru.shanalotte.serviceregistry.domain.MorriganPlatformService;
import ru.shanalotte.serviceregistry.domain.MorriganPlatformServiceUptime;
import ru.shanalotte.serviceregistry.dto.MorriganServiceHeartbeat;
import ru.shanalotte.serviceregistry.dto.MorriganServiceRegistration;
import ru.shanalotte.serviceregistry.mybatis.mappers.ServiceMapper;
import ru.shanalotte.serviceregistry.mybatis.mappers.ServiceUptimeMapper;

@Service
public class ServicesDAOImpl implements ServicesDAO {

  @Value("classpath:mybatis-config.xml")
  private Resource resource;

  private SqlSessionFactory sqlSessionFactory;

  @PostConstruct
  public void initSessionFactory() throws IOException {
    sqlSessionFactory = new SqlSessionFactoryBuilder().build(resource.getInputStream());
  }

  @Override
  public long create(MorriganServiceRegistration dto) {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    ServiceMapper mapper = sqlSession.getMapper(ServiceMapper.class);
    MorriganPlatformService serviceWithSameNameAndLastNumber = mapper.findMaxNumberByName(dto.getName());
    int number = serviceWithSameNameAndLastNumber == null ? 1 : serviceWithSameNameAndLastNumber.getNumber() + 1;
    MorriganPlatformService service = MorriganPlatformService.builder()
        .host(dto.getHost())
        .name(dto.getName())
        .number(number)
        .build();
    long id = mapper.addNewService(service);
    ServiceUptimeMapper serviceUptimeMapper = sqlSession.getMapper(ServiceUptimeMapper.class);
    serviceUptimeMapper.createUptime(id);
    sqlSession.commit();
    sqlSession.close();
    return id;
  }

  @Override
  public void abandon(long id) {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    ServiceMapper mapper = sqlSession.getMapper(ServiceMapper.class);
    mapper.setInactive(id);
    sqlSession.commit();
    sqlSession.close();
  }

  @Override
  public MorriganPlatformService findById(Long id) {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    ServiceMapper mapper = sqlSession.getMapper(ServiceMapper.class);
    var service = mapper.findById(id);
    sqlSession.commit();
    sqlSession.close();
    return service;
  }

  @Override
  public void refreshUptime(long id) {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    ServiceUptimeMapper mapper = sqlSession.getMapper(ServiceUptimeMapper.class);
    var uptime = mapper.findById(id);
    mapper.refreshUptime(id, 1, 1);
    sqlSession.commit();
    sqlSession.close();
  }

  @Override
  public List<MorriganPlatformService> findActiveServicesByName(String name) {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    ServiceMapper mapper = sqlSession.getMapper(ServiceMapper.class);
    var services = mapper.findActiveByName(name);
    sqlSession.commit();
    sqlSession.close();
    return services;
  }

  @Override
  public MorriganPlatformServiceUptime getUptime(long id) {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    ServiceUptimeMapper mapper = sqlSession.getMapper(ServiceUptimeMapper.class);
    var uptime = mapper.findById(id);
    sqlSession.commit();
    sqlSession.close();
    return uptime;
  }
}
