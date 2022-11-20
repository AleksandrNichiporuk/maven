package com.andev.entity;

import com.andev.util.HibernateTestUtil;
import com.andev.util.TestEntity;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {
    private static final SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();

    @Test
    void whenSave_thenSaveCorrect() {
        Product givenProduct = TestEntity.getTestProduct();
        Manufacturer givenManufacturer = TestEntity.getTestManufacturer();

        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        givenManufacturer.addProduct(givenProduct);
        Serializable savedManufacturerId = session.save(givenManufacturer);
        session.clear();
        Manufacturer actualManufacturer = session.get(Manufacturer.class, savedManufacturerId);

        assertThat(actualManufacturer.getProducts()).isNotNull();

        session.getTransaction().rollback();
    }

    @Test
    void whenFindById_thenReturnEntity() {
        Product givenProduct = TestEntity.getTestProduct();
        Manufacturer givenManufacturer = TestEntity.getTestManufacturer();

        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        givenManufacturer.addProduct(givenProduct);
        Serializable savedManufacturerId = session.save(givenManufacturer);
        session.clear();
        Manufacturer actualManufacturer = session.get(Manufacturer.class, savedManufacturerId);


        assertThat(actualManufacturer.getProducts()).hasSize(1);

        session.getTransaction().rollback();
    }

    @Test
    void whenFindById_thenNotFound() {
        Product givenProduct = TestEntity.getTestProduct();
        Manufacturer givenManufacturer = TestEntity.getTestManufacturer();

        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        givenManufacturer.addProduct(givenProduct);
        session.save(givenManufacturer);
        session.clear();
        Manufacturer maybeManufacturer = session.get(Manufacturer.class, Integer.MAX_VALUE);

        assertThat(maybeManufacturer).isNull();

        session.getTransaction().rollback();
    }

    @Test
    void whenUpdate_thenOk() {
        Product givenProduct = TestEntity.getTestProduct();
        Manufacturer givenManufacturer = TestEntity.getTestManufacturer();

        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        givenManufacturer.addProduct(givenProduct);
        Serializable savedManufacturerId = session.save(givenManufacturer);
        session.clear();
        Manufacturer actualManufacturer = session.get(Manufacturer.class, savedManufacturerId);

        actualManufacturer.getProducts().get(0).setModel("iPhone 12");
        actualManufacturer.getProducts().get(0).setAmount(25);
        actualManufacturer.getProducts().get(0).setPrice(BigDecimal.valueOf(800, 2));
        session.update(actualManufacturer);
        session.flush();
        session.clear();
        Manufacturer updatedManufacturer = session.get(Manufacturer.class, savedManufacturerId);

        assertThat(updatedManufacturer.getProducts().get(0).getModel()).isEqualTo("iPhone 12");
        assertThat(updatedManufacturer.getProducts().get(0).getAmount()).isEqualTo(25);
        assertThat(updatedManufacturer.getProducts().get(0).getPrice()).isEqualTo(BigDecimal.valueOf(800, 2));

        session.getTransaction().rollback();
    }


    @Test
    void whenDelete_thenOk() {
        Product givenProduct = TestEntity.getTestProduct();
        Manufacturer givenManufacturer = TestEntity.getTestManufacturer();

        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        givenManufacturer.addProduct(givenProduct);
        Serializable savedManufacturerId = session.save(givenManufacturer);
        session.clear();
        Manufacturer actualManufacturer = session.get(Manufacturer.class, savedManufacturerId);
        Product actualProduct = actualManufacturer.getProducts().get(0);
        actualManufacturer.getProducts().removeIf(product -> product.getId().equals(actualProduct.getId()));
        session.flush();
        session.clear();
        Manufacturer actualManufacturer2 = session.get(Manufacturer.class, savedManufacturerId);

        assertThat(actualManufacturer2.getProducts()).hasSize(0);

        session.getTransaction().rollback();
    }
}