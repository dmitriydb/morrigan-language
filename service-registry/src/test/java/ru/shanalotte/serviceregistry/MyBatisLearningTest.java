package ru.shanalotte.serviceregistry;

import java.io.IOException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import ru.shanalotte.serviceregistry.domain.MorriganPlatformService;
import ru.shanalotte.serviceregistry.mybatis.mappers.ServiceMapper;

@SpringBootTest
@ActiveProfiles("dev")
public class MyBatisLearningTest {

  @Value("classpath:mybatis-config.xml")
  private Resource resource;

  @Test
  public void test() throws IOException {
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resource.getInputStream());
    SqlSession sqlSession = sqlSessionFactory.openSession();

    MorriganPlatformService service = MorriganPlatformService.builder()
        .host("test")
        .name("testname")
        .number(1)
        .build();
    ServiceMapper mapper = sqlSession.getMapper(ServiceMapper.class);
    mapper.addNewService(service);
    sqlSession.commit();
    sqlSession.close();
  }
}
