package com.andev.dao;

import com.andev.dto.ProductFilter;
import com.andev.entity.Manufacturer_;
import com.andev.entity.Product;
import com.andev.entity.Product_;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

import static com.andev.entity.QManufacturer.manufacturer;
import static com.andev.entity.QProduct.product;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao {
    private static final UserDao INSTANCE = new UserDao();

    public static UserDao getInstance() {
        return INSTANCE;
    }

    public List<Product> findProductByFilter_querydsl(Session session, ProductFilter filter) {

        Predicate predicate = QPredicate.builder()
                .add(filter.getProductName(), product.name::eq)
                .add(filter.getManufacturerName(), manufacturer.name::eq)
                .add(filter.getPriceMin(), product.price::gt)
                .add(filter.getPriceMax(), product.price::lt)
                .add(filter.getCategory(), product.category::eq)
                .buildAnd();

        RootGraph<Product> productGraph = session.createEntityGraph(Product.class);
        productGraph.addAttributeNode("manufacturer");


        return new JPAQuery<Product>(session)
                .select(product)
                .from(product)
                .join(product.manufacturer, manufacturer)
                .where(predicate)
                .setHint(GraphSemantic.FETCH.getJpaHintName(), productGraph)
                .fetch();
    }

    public List<Product> findProductByFilter_criteria(Session session, ProductFilter filter) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = cb.createQuery(Product.class);
        Root<Product> product = criteria.from(Product.class);
        Join<Object, Object> manufacturer = product.join(Product_.MANUFACTURER);

        List<javax.persistence.criteria.Predicate> predicates = CriteriaPredicate.builder()
                .add(filter.getProductName(), prodName -> cb.equal(product.get(Product_.NAME), prodName))
                .add(filter.getManufacturerName(), mnfName -> cb.equal(manufacturer.get(Manufacturer_.NAME), mnfName))
                .add(filter.getPriceMin(), prMin -> cb.gt(product.get(Product_.PRICE), prMin))
                .add(filter.getPriceMax(), prMax -> cb.lt(product.get(Product_.PRICE), prMax))
                .add(filter.getCategory(), category -> cb.equal(product.get(Product_.CATEGORY), category))
                .getPredicates();

        criteria.select(product).where(predicates.toArray(javax.persistence.criteria.Predicate[]::new));

        RootGraph<Product> productGraph = session.createEntityGraph(Product.class);
        productGraph.addAttributeNode("manufacturer");

        return session.createQuery(criteria)
                .setHint(GraphSemantic.FETCH.getJpaHintName(), productGraph)
                .list();
    }
}
