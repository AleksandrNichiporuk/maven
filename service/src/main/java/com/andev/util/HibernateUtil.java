package com.andev.util;

import com.andev.entity.*;
import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {

    public static Session buildSession() {
        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Order.class);
        configuration.addAnnotatedClass(Product.class);
        configuration.addAnnotatedClass(UserInfo.class);
        configuration.addAnnotatedClass(Role.class);
        configuration.configure();
        return configuration.buildSessionFactory().openSession();
    }
}
