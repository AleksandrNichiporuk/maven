package com.andev.config;

import com.andev.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.lang.reflect.Proxy;

@Configuration
@ComponentScan(basePackages = "com.andev")
public class ApplicationConfigurationTest {

    @Bean
    public SessionFactory sessionFactory() {
        return HibernateTestUtil.buildSessionFactory();
    }

    @Bean
    public Session session(SessionFactory sessionFactory) {
        return (Session) Proxy.newProxyInstance(sessionFactory.getClass().getClassLoader(), new Class[]{Session.class}, (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));
    }

    @PreDestroy
    public void closeSF() {
        sessionFactory().close();
    }
}
