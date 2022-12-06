package com.andev.dao;

import com.andev.entity.UserInfo;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository
public class UserInfoRepository extends RepositoryBase<Integer, UserInfo> {

    public UserInfoRepository(Session session) {
        super(UserInfo.class, session);
    }
}
