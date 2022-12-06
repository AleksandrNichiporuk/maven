package com.andev.dao;

import com.andev.entity.User;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends RepositoryBase<Integer, User> {

    public UserRepository(Session session) {
        super(User.class, session);
    }
}
