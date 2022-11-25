package com.andev.dao;

import com.andev.entity.Order;
import com.andev.util.HibernateTestUtil;
import com.andev.util.ProxySession;
import com.andev.util.TestEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class OrderRepositoryTest {
    private final SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();
    private final Session session = ProxySession.makeProxy(sessionFactory);
    private final OrderRepository repository = new OrderRepository(session);

    @Test
    void whenSaveThenReturnEntity() {
        session.beginTransaction();

        Order givenOrder = TestEntity.getFirstTestOrder();
        Order savedOrder = repository.save(givenOrder);
        session.flush();
        session.clear();
        Order actualOrder = session.get(Order.class, savedOrder.getId());

        assertThat(actualOrder.getId()).isNotNull();

        session.getTransaction().rollback();
    }

    @Test
    void whenDeleteThenOk() {
        session.beginTransaction();

        Order givenOrder = TestEntity.getFirstTestOrder();
        Serializable savedId = session.save(givenOrder);
        session.flush();
        session.clear();
        Order actualOrder = session.get(Order.class, savedId);
        repository.delete(actualOrder);
        session.clear();
        Order deletedOrder = session.get(Order.class, savedId);

        assertThat(deletedOrder).isNull();

        session.getTransaction().rollback();
    }

    @Test
    void whenUpdateThenReturnUpdatedEntity() {
        session.beginTransaction();

        Order givenOrder = TestEntity.getFirstTestOrder();
        Serializable savedId = session.save(givenOrder);
        session.flush();
        session.clear();
        Order actualOrder = session.get(Order.class, savedId);
        actualOrder.setTotalValue(222);
        repository.update(actualOrder);
        session.flush();
        session.clear();
        session.get(Order.class, savedId);

        assertThat(actualOrder.getTotalValue()).isEqualTo(222);

        session.getTransaction().rollback();
    }

    @Test
    void whenFindByIdWhenReturnEntity() {
        session.beginTransaction();

        Order givenOrder = TestEntity.getFirstTestOrder();
        Serializable savedId = session.save(givenOrder);
        session.flush();
        session.clear();
        Optional<Order> actualOrder = repository.findById((Integer) savedId);

        assertThat(actualOrder).isPresent();
        actualOrder.ifPresent(order->assertThat(order.getId()).isEqualTo(savedId));

        session.getTransaction().rollback();
    }

    @Test
    void whenFindByWrongIdWhenNotFound() {
        session.beginTransaction();

        Order givenOrder = TestEntity.getFirstTestOrder();
        session.save(givenOrder);
        session.flush();
        session.clear();
        Optional<Order> maybeOrder = repository.findById(Integer.MAX_VALUE);

        assertThat(maybeOrder).isEmpty();

        session.getTransaction().rollback();
    }

    @Test
    void whenFindAllThenReturnListEntities() {
        session.beginTransaction();

        Order givenOrder = TestEntity.getFirstTestOrder();
        session.save(givenOrder);
        session.flush();
        session.clear();
        List<Order> actual = repository.findAll();

        assertThat(actual.size()).isEqualTo(1);

        session.getTransaction().rollback();
    }
}