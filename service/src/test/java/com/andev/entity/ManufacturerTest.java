package com.andev.entity;

import com.andev.util.HibernateTestUtil;
import com.andev.util.TestEntity;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.assertj.core.api.Assertions.assertThat;

class ManufacturerTest {
    private static final SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();

    @Test
    void whenSave_thenSaveCorrect() {
        Manufacturer givenManufacturer = TestEntity.getTestManufacturer();

        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Serializable savedManufacturerId = session.save(givenManufacturer);
        session.clear();
        Manufacturer actualManufacturer = session.get(Manufacturer.class, savedManufacturerId);

        assertThat(actualManufacturer).isNotNull();

        session.getTransaction().rollback();
    }

    @Test
    void whenFindById_thenReturnEntity() {
        Manufacturer givenManufacturer = TestEntity.getTestManufacturer();

        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Serializable savedManufacturerId = session.save(givenManufacturer);
        session.clear();
        Manufacturer actualManufacturer = session.get(Manufacturer.class, savedManufacturerId);

        assertThat(actualManufacturer.getId()).isEqualTo(savedManufacturerId);

        session.getTransaction().rollback();
    }

    @Test
    void whenFindById_thenNotFound() {
        Manufacturer givenManufacturer = TestEntity.getTestManufacturer();

        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Serializable savedManufacturerId = session.save(givenManufacturer);
        session.clear();
        Manufacturer maybeManufacturer = session.get(Manufacturer.class, Integer.MAX_VALUE);

        assertThat(maybeManufacturer).isNull();

        session.getTransaction().rollback();
    }

    @Test
    void whenUpdate_thenOk() {
        Manufacturer givenManufacturer = TestEntity.getTestManufacturer();

        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Serializable savedManufacturerId = session.save(givenManufacturer);
        session.clear();
        Manufacturer actualManufacturer = session.get(Manufacturer.class, savedManufacturerId);
        actualManufacturer.setName("Sony");
        session.update(actualManufacturer);
        session.flush();
        session.clear();
        Manufacturer updatedManufacturer = session.get(Manufacturer.class, savedManufacturerId);

        assertThat(updatedManufacturer.getName()).isEqualTo("Sony");

        session.getTransaction().rollback();
    }


    @Test
    void whenDelete_thenOk() {
        Manufacturer givenManufacturer = TestEntity.getTestManufacturer();

        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Serializable savedManufacturerId = session.save(givenManufacturer);
        session.clear();
        Manufacturer actualManufacturer = session.get(Manufacturer.class, savedManufacturerId);
        session.delete(actualManufacturer);
        session.flush();
        Manufacturer deletedManufacturer = session.get(Manufacturer.class, savedManufacturerId);

        assertThat(deletedManufacturer).isNull();

        session.getTransaction().rollback();
    }
}
