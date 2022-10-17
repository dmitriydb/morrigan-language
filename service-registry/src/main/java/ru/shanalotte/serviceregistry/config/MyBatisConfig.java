package ru.shanalotte.serviceregistry.config;

import java.io.IOException;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

@Configuration
public class MyBatisConfig {

  @Value("classpath:mybatis-config.xml")
  private Resource resource;

  @Bean
  public SqlSessionFactory sqlSessionFactory() throws IOException {
    return new SqlSessionFactoryBuilder().build(resource.getInputStream());
  }
}
