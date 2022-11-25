package com.andev.dao;

import com.andev.entity.UserInfo;
import org.hibernate.Session;

public class UserInfoRepository extends RepositoryBase<Integer, UserInfo> {

    public UserInfoRepository(Session session) {
        super(UserInfo.class, session);
    }
}
