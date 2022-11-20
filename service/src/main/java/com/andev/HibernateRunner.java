package com.andev;

import com.andev.entity.Order;
import com.andev.entity.enumred.Status;
import com.andev.entity.User;
import com.andev.util.HibernateUtil;
import org.hibernate.Session;

import java.time.LocalDate;

public class HibernateRunner {

    public static void main(String[] args) {

        User user = User.builder()
                .login("Ivan")
                .password("Ivanov")
                .build();

        Order order = Order.builder()
                .dateClosing(LocalDate.of(2000, 1, 1))
                .dateOrder(LocalDate.of(2000, 1, 1))
                .status(Status.RECEIVED)
                .build();

        try (Session session = HibernateUtil.buildSession()) {
            session.getTransaction().begin();
            session.save(user);
            session.getTransaction().commit();
            session.close();
        }

        try (Session session = HibernateUtil.buildSession()) {
            session.getTransaction().begin();
            User user1 = session.get(User.class, 1);
            session.delete(user1);
            session.getTransaction().commit();
        }

//        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
//            Session session = sessionFactory.openSession();
//            session.getTransaction().begin();
//            session.save(order);
//            session.getTransaction().commit();
//        }
    }
}
