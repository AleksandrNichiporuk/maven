package com.andev.util;

import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.lang.reflect.Proxy;

@UtilityClass
public class ProxySession {

    public Session makeProxy(SessionFactory sessionFactory) {
        return (Session) Proxy.newProxyInstance(sessionFactory.getClass().getClassLoader(), new Class[]{Session.class},
                (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));
    }
}
