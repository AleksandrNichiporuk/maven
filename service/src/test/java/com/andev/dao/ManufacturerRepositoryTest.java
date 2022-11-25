package com.andev.dao;

import com.andev.entity.Manufacturer;
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
import static org.junit.jupiter.api.Assertions.assertAll;

class ManufacturerRepositoryTest {

    private final SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();
    private final Session session = ProxySession.makeProxy(sessionFactory);
    private final ManufacturerRepository repository = new ManufacturerRepository(session);

    @Test
    void whenSaveThenReturnEntity() {
        session.beginTransaction();

        Manufacturer givenManufacturer = TestEntity.getTestManufacturer();
        Manufacturer savedManufacturer = repository.save(givenManufacturer);
        session.flush();
        session.clear();
        Manufacturer actualManufacturer = session.get(Manufacturer.class, savedManufacturer.getId());

        assertThat(actualManufacturer.getId()).isNotNull();

        session.getTransaction().rollback();
    }

    @Test
    void whenDeleteThenOk() {
        session.beginTransaction();

        Manufacturer givenManufacturer = TestEntity.getTestManufacturer();
        Serializable savedId = session.save(givenManufacturer);
        session.flush();
        session.clear();
        Manufacturer actualManufacturer = session.get(Manufacturer.class, savedId);
        repository.delete(actualManufacturer);
        session.clear();
        Manufacturer deletedManufacturer = session.get(Manufacturer.class, savedId);

        assertThat(deletedManufacturer).isNull();

        session.getTransaction().rollback();
    }

    @Test
    void whenUpdateThenReturnUpdatedEntity() {
        session.beginTransaction();

        Manufacturer givenManufacturer = TestEntity.getTestManufacturer();
        Serializable savedId = session.save(givenManufacturer);
        session.flush();
        session.clear();
        Manufacturer actualManufacturer = session.get(Manufacturer.class, savedId);
        actualManufacturer.setName("Updated");
        actualManufacturer.setDescription("updated description");
        repository.update(actualManufacturer);
        session.flush();
        session.clear();
        session.get(Manufacturer.class, savedId);

        assertAll(
                () -> assertThat(actualManufacturer.getName()).isEqualTo("Updated"),
                () -> assertThat(actualManufacturer.getDescription()).isEqualTo("updated description")
        );

        session.getTransaction().rollback();
    }

    @Test
    void whenFindByIdWhenReturnEntity() {
        session.beginTransaction();

        Manufacturer givenManufacturer = TestEntity.getTestManufacturer();
        Serializable savedId = session.save(givenManufacturer);
        session.flush();
        session.clear();
        Optional<Manufacturer> actualManufacturer = repository.findById((Integer) savedId);

        assertThat(actualManufacturer).isPresent();
        actualManufacturer.ifPresent(mnf -> assertThat(mnf.getId()).isEqualTo(savedId));

        session.getTransaction().rollback();
    }

    @Test
    void whenFindByWrongIdWhenNotFound() {
        session.beginTransaction();

        Manufacturer givenManufacturer = TestEntity.getTestManufacturer();
        session.save(givenManufacturer);
        session.flush();
        session.clear();
        Optional<Manufacturer> maybeManufacturer = repository.findById(Integer.MAX_VALUE);

        assertThat(maybeManufacturer).isEmpty();

        session.getTransaction().rollback();
    }

    @Test
    void whenFindAllThenReturnListEntities() {
        session.beginTransaction();

        Manufacturer givenManufacturer = TestEntity.getTestManufacturer();
        session.save(givenManufacturer);
        session.flush();
        session.clear();
        List<Manufacturer> actual = repository.findAll();

        assertThat(actual).hasSize(1);

        session.getTransaction().rollback();
    }
}