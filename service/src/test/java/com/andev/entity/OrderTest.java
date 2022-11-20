package com.andev.entity;

import com.andev.util.HibernateTestUtil;
import com.andev.util.TestEntity;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {
    private static final SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();

    @Test
    void whenSave_thenSaveCorrect() {
        User givenUser = TestEntity.getTestUser();
        Order givenFirstOrder = TestEntity.getFirstTestOrder();
        Order givenSecondOrder = TestEntity.getSecondTestOrder();

        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        givenUser.addOrder(givenFirstOrder);
        givenUser.addOrder(givenSecondOrder);
        Serializable savedUserId = session.save(givenUser);
        session.clear();
        User actualUser = session.get(User.class, savedUserId);

        assertThat(actualUser.getOrders()).hasSize(2);

        session.getTransaction().rollback();
    }


    @Test
    void whenFindById_thenReturnEntity() {
        User givenUser = TestEntity.getTestUser();
        Order givenFirstOrder = TestEntity.getFirstTestOrder();
        Order givenSecondOrder = TestEntity.getSecondTestOrder();

        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        givenUser.addOrder(givenFirstOrder);
        givenUser.addOrder(givenSecondOrder);
        Serializable savedUserId = session.save(givenUser);
        session.clear();
        User actualUser = session.get(User.class, savedUserId);

        assertThat(actualUser.getOrders()).hasSize(2);

        session.getTransaction().rollback();
    }

    @Test
    void whenFindById_thenNotFound() {
        User givenUser = TestEntity.getTestUser();
        Order givenFirstOrder = TestEntity.getFirstTestOrder();
        Order givenSecondOrder = TestEntity.getSecondTestOrder();

        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        givenUser.addOrder(givenFirstOrder);
        givenUser.addOrder(givenSecondOrder);
        session.save(givenUser);
        session.clear();
        User maybeUser = session.get(User.class, Integer.MAX_VALUE);

        assertThat(maybeUser).isNull();

        session.getTransaction().rollback();
    }

    @Test
    void whenUpdate_thenOk() {
        User givenUser = TestEntity.getTestUser();
        Order givenFirstOrder = TestEntity.getFirstTestOrder();
        Order givenSecondOrder = TestEntity.getSecondTestOrder();

        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        givenUser.addOrder(givenFirstOrder);
        givenUser.addOrder(givenSecondOrder);
        Serializable savedUserId = session.save(givenUser);
        session.clear();
        User actualUser = session.get(User.class, savedUserId);
        actualUser.getOrders().get(0).setTotalValue(222);
        actualUser.getOrders().get(1).setTotalValue(333);
        session.update(actualUser);
        session.flush();
        session.clear();
        User updatedUser = session.get(User.class, savedUserId);

        assertThat(updatedUser.getOrders().get(0).getTotalValue()).isEqualTo(222);
        assertThat(updatedUser.getOrders().get(1).getTotalValue()).isEqualTo(333);

        session.getTransaction().rollback();
    }

    @Test
    void whenDelete_thenOk() {
        User givenUser = TestEntity.getTestUser();
        Order givenFirstOrder = TestEntity.getFirstTestOrder();
        Order givenSecondOrder = TestEntity.getSecondTestOrder();

        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        givenUser.addOrder(givenFirstOrder);
        givenUser.addOrder(givenSecondOrder);
        Serializable savedUserId = session.save(givenUser);
        session.clear();
        User actualUser = session.get(User.class, savedUserId);
        Order actualFirstOrder = actualUser.getOrders().get(0);
        actualUser.getOrders().removeIf(order -> order.getId().equals(actualFirstOrder.getId()));
        session.flush();
        session.clear();
        User actualUser2 = session.get(User.class, savedUserId);

        assertThat(actualUser2.getOrders()).hasSize(1);

        session.getTransaction().rollback();
    }

}