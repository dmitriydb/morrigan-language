package ru.shanalotte.serviceregistry.client;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContext implements ApplicationContextAware {

  private static ApplicationContext context;

  public static <T> T getBean(Class<T> beanClass) {
    return context.getBean(beanClass);
  }

  private static synchronized void setContext(ApplicationContext context) {
    SpringContext.context = context;
  }

  @Override
  public void setApplicationContext(ApplicationContext context) throws BeansException {
    setContext(context);
  }
}