package com.andev.dao;

import com.andev.dto.ProductFilter;
import com.andev.entity.Product;
import com.andev.entity.enums.Category;
import com.andev.util.HibernateTestUtil;
import com.andev.util.TestDataImporter;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(Lifecycle.PER_CLASS)
class UserDaoTest {

    private final SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();
    private final UserDao userDao = UserDao.getInstance();

    @BeforeAll
    void initDb() {
        TestDataImporter.importData(sessionFactory);
    }

    @AfterAll
    void closeSf() {
        sessionFactory.close();
    }

    @Test
    void whenFindProductByQuerydslAndFilterOfManufacturerAndCategory_thenReturnListProducts() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        ProductFilter filter = ProductFilter.builder()
                .manufacturerName("Samsung")
                .category(Category.ELECTRONICS)
                .build();
        List<Product> filtedProducts = userDao.findProductByFilter_querydsl(session, filter);
        assertThat(filtedProducts.size()).isEqualTo(1);
        assertThat(filtedProducts.get(0).getModel()).isEqualTo("Galaxy S22");

        session.getTransaction().rollback();
    }

    @Test
    void whenFindProductByQuerydslAndFilterOfNameAndPrice_thenReturnListProducts() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        ProductFilter filter = ProductFilter.builder()
                .productName("smartphone")
                .priceMin(BigDecimal.valueOf(740))
                .priceMax(BigDecimal.valueOf(900))
                .build();
        List<Product> filtedProducts = userDao.findProductByFilter_querydsl(session, filter);
        assertThat(filtedProducts.size()).isEqualTo(2);

        List<String> models = filtedProducts.stream().map(Product::getModel).collect(Collectors.toList());
        assertThat(models).containsExactlyInAnyOrder("iPhone 11","Galaxy S22");

        session.getTransaction().rollback();
    }

    @Test
    void whenFindProductByQuerydslAndEmptyFilter_thenReturnListOfAllProducts() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        ProductFilter filterEmpty = ProductFilter.builder().build();
        List<Product> products = userDao.findProductByFilter_querydsl(session, filterEmpty);
        assertThat(products.size()).isEqualTo(6);

        session.getTransaction().rollback();
    }

    @Test
    void whenFindProductByCriteriaAndFilterOfManufacturerAndCategory_thenReturnListProducts() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        ProductFilter filter = ProductFilter.builder()
                .manufacturerName("Samsung")
                .category(Category.ELECTRONICS)
                .build();
        List<Product> filtedProducts = userDao.findProductByFilter_criteria(session, filter);
        assertThat(filtedProducts.size()).isEqualTo(1);
        assertThat(filtedProducts.get(0).getModel()).isEqualTo("Galaxy S22");

        session.getTransaction().rollback();
    }

    @Test
    void whenFindProductByCriteriaAndFilterOfNameAndPrice_thenReturnListProducts() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        ProductFilter filter = ProductFilter.builder()
                .productName("smartphone")
                .priceMin(BigDecimal.valueOf(740))
                .priceMax(BigDecimal.valueOf(900))
                .build();
        List<Product> filtedProducts = userDao.findProductByFilter_criteria(session, filter);
        assertThat(filtedProducts.size()).isEqualTo(2);

        List<String> models = filtedProducts.stream().map(Product::getModel).collect(Collectors.toList());
        assertThat(models).containsExactlyInAnyOrder("iPhone 11","Galaxy S22");

        session.getTransaction().rollback();
    }

    @Test
    void whenFindProductByCriteriaAndEmptyFilter_thenReturnListOfAllProducts() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        ProductFilter filterEmpty = ProductFilter.builder().build();
        List<Product> products = userDao.findProductByFilter_criteria(session, filterEmpty);
        assertThat(products.size()).isEqualTo(6);

        session.getTransaction().rollback();
    }

}