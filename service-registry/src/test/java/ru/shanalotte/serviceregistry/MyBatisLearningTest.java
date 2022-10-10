package ru.shanalotte.serviceregistry;

import java.util.List;
import javax.sql.DataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.shanalotte.serviceregistry.domain.MorriganPlatformService;
import ru.shanalotte.serviceregistry.mybatis.mappers.ServiceMapper;
import ru.shanalotte.serviceregistry.mybatis.mappers.ServiceUptimeMapper;

@SpringBootTest
@Testcontainers
public class MyBatisLearningTest {

  @Container
  private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
      new PostgreSQLContainer<>("postgres:11")
          .withInitScript("schema.sql");


  @Test
  public void shouldWork() {
    // Get DataSource object.
    DataSource dataSource = getDataSource();

    // Creates a transaction factory.
    TransactionFactory trxFactory = new JdbcTransactionFactory();

    // Creates an environment object with the specified name, transaction
    // factory and a data source.
    Environment env = new Environment("dev", trxFactory, dataSource);

    // Creates a Configuration object base on the Environment object.
    // We can also add type aliases and mappers.
    Configuration config = new Configuration(env);
    TypeAliasRegistry aliases = config.getTypeAliasRegistry();

    config.addMapper(ServiceMapper.class);
    config.addMapper(ServiceUptimeMapper.class);
    SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(config);

    try (SqlSession session = factory.openSession()) {
      ServiceMapper serviceMapper = session.getMapper(ServiceMapper.class);
      List<MorriganPlatformService> services = serviceMapper.findAll();
      System.out.println(services.size());
    }
  }

  public DataSource getDataSource() {
    PGSimpleDataSource dataSource = new PGSimpleDataSource();
    dataSource.setUrl(POSTGRESQL_CONTAINER.getJdbcUrl());
    dataSource.setUser(POSTGRESQL_CONTAINER.getUsername());
    dataSource.setPassword(POSTGRESQL_CONTAINER.getPassword());
    return dataSource;
  }
}
