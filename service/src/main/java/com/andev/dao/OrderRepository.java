package com.andev.dao;

import com.andev.entity.Order;
import org.hibernate.Session;

public class OrderRepository extends RepositoryBase<Integer, Order> {

    public OrderRepository(Session session) {
        super(Order.class, session);
    }
}
