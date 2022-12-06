package com.andev.dao;

import com.andev.config.ApplicationConfigurationTest;
import com.andev.dto.ProductFilter;
import com.andev.entity.Product;
import com.andev.entity.enums.Category;
import com.andev.util.TestDataImporter;
import com.andev.util.TestEntity;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductRepositoryTest {
    private final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfigurationTest.class);
    private final ProductRepository repository = context.getBean(ProductRepository.class);

    private final Session session = context.getBean(Session.class);

    @BeforeAll
    void initDb() {
        TestDataImporter.importData(session);
    }

    @AfterAll
    void closeContext() {
        context.close();
    }

    @Test
    void whenSaveThenReturnEntity() {
        session.beginTransaction();

        Product givenProduct = TestEntity.getTestProduct();
        Product savedProduct = repository.save(givenProduct);
        session.flush();
        session.clear();
        Product actualProduct = session.get(Product.class, savedProduct.getId());

        assertThat(actualProduct.getId()).isNotNull();

        session.getTransaction().rollback();
    }

    @Test
    void whenDeleteThenOk() {
        session.beginTransaction();

        Product givenProduct = TestEntity.getTestProduct();
        Serializable savedId = session.save(givenProduct);
        session.flush();
        session.clear();
        Product actualProduct = session.get(Product.class, savedId);
        repository.delete(actualProduct);
        session.clear();
        Product deletedProduct = session.get(Product.class, savedId);

        assertThat(deletedProduct).isNull();

        session.getTransaction().rollback();
    }

    @Test
    void whenUpdateThenReturnUpdatedEntity() {
        session.beginTransaction();

        Product givenProduct = TestEntity.getTestProduct();
        Serializable savedId = session.save(givenProduct);
        session.flush();
        session.clear();
        Product actualProduct = session.get(Product.class, savedId);
        actualProduct.setModel("iPhone 12");
        actualProduct.setAmount(36);
        actualProduct.setPrice(BigDecimal.valueOf(875, 2));
        repository.update(actualProduct);
        session.flush();
        session.clear();
        session.get(Product.class, savedId);

        Assertions.assertAll(
                () -> assertThat(actualProduct.getModel()).isEqualTo("iPhone 12"),
                () -> assertThat(actualProduct.getAmount()).isEqualTo(36),
                () -> assertThat(actualProduct.getPrice()).isEqualTo(BigDecimal.valueOf(875, 2))
        );

        session.getTransaction().rollback();
    }

    @Test
    void whenFindByIdWhenReturnEntity() {
        session.beginTransaction();

        Product givenProduct = TestEntity.getTestProduct();
        Serializable savedId = session.save(givenProduct);
        session.flush();
        session.clear();
        Optional<Product> actualProduct = repository.findById((Integer) savedId);

        assertThat(actualProduct).isPresent();
        actualProduct.ifPresent(prod -> assertThat(prod.getId()).isEqualTo(savedId));

        session.getTransaction().rollback();
    }

    @Test
    void whenFindByWrongIdWhenNotFound() {
        session.beginTransaction();

        Product givenProduct = TestEntity.getTestProduct();
        session.save(givenProduct);
        session.flush();
        session.clear();
        Optional<Product> maybeProduct = repository.findById(Integer.MAX_VALUE);

        assertThat(maybeProduct).isEmpty();

        session.getTransaction().rollback();
    }

    @Test
    void whenFindAllThenReturnListEntities() {
        session.beginTransaction();

        List<Product> actual = repository.findAll();
        assertThat(actual.size()).isEqualTo(6);

        session.getTransaction().rollback();
    }

    @Test
    void whenFindProductByQuerydslAndFilterOfManufacturerAndCategory_thenReturnListProducts() {
        session.beginTransaction();

        ProductFilter filter = ProductFilter.builder()
                .manufacturerName("Samsung")
                .category(Category.ELECTRONICS)
                .build();
        List<Product> filtedProducts = repository.findProductByFilter_querydsl(session, filter);
        assertThat(filtedProducts.size()).isEqualTo(1);
        assertThat(filtedProducts.get(0).getModel()).isEqualTo("Galaxy S22");

        session.getTransaction().rollback();
    }

    @Test
    void whenFindProductByQuerydslAndFilterOfNameAndPrice_thenReturnListProducts() {
        session.beginTransaction();

        ProductFilter filter = ProductFilter.builder()
                .productName("smartphone")
                .priceMin(BigDecimal.valueOf(740))
                .priceMax(BigDecimal.valueOf(900))
                .build();
        List<Product> filtedProducts = repository.findProductByFilter_querydsl(session, filter);
        assertThat(filtedProducts.size()).isEqualTo(2);

        List<String> models = filtedProducts.stream().map(Product::getModel).collect(Collectors.toList());
        assertThat(models).containsExactlyInAnyOrder("iPhone 11", "Galaxy S22");

        session.getTransaction().rollback();
    }

    @Test
    void whenFindProductByQuerydslAndEmptyFilter_thenReturnListOfAllProducts() {
        session.beginTransaction();

        ProductFilter filterEmpty = ProductFilter.builder().build();
        List<Product> products = repository.findProductByFilter_querydsl(session, filterEmpty);
        assertThat(products.size()).isEqualTo(6);

        session.getTransaction().rollback();
    }

    @Test
    void whenFindProductByCriteriaAndFilterOfManufacturerAndCategory_thenReturnListProducts() {
        session.beginTransaction();

        ProductFilter filter = ProductFilter.builder()
                .manufacturerName("Samsung")
                .category(Category.ELECTRONICS)
                .build();
        List<Product> filtedProducts = repository.findProductByFilter_criteria(session, filter);
        assertThat(filtedProducts.size()).isEqualTo(1);
        assertThat(filtedProducts.get(0).getModel()).isEqualTo("Galaxy S22");

        session.getTransaction().rollback();
    }

    @Test
    void whenFindProductByCriteriaAndFilterOfNameAndPrice_thenReturnListProducts() {
        session.beginTransaction();

        ProductFilter filter = ProductFilter.builder()
                .productName("smartphone")
                .priceMin(BigDecimal.valueOf(740))
                .priceMax(BigDecimal.valueOf(900))
                .build();
        List<Product> filtedProducts = repository.findProductByFilter_criteria(session, filter);
        assertThat(filtedProducts.size()).isEqualTo(2);

        List<String> models = filtedProducts.stream().map(Product::getModel).collect(Collectors.toList());
        assertThat(models).containsExactlyInAnyOrder("iPhone 11", "Galaxy S22");

        session.getTransaction().rollback();
    }

    @Test
    void whenFindProductByCriteriaAndEmptyFilter_thenReturnListOfAllProducts() {
        session.beginTransaction();

        ProductFilter filterEmpty = ProductFilter.builder().build();
        List<Product> products = repository.findProductByFilter_criteria(session, filterEmpty);
        assertThat(products.size()).isEqualTo(6);

        session.getTransaction().rollback();
    }
}