package com.andev.dao;

import com.andev.entity.UserAddress;
import com.andev.entity.UserInfo;
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

class UserInfoRepositoryTest {
    private final SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();
    private final Session session = ProxySession.makeProxy(sessionFactory);
    private final UserInfoRepository repository = new UserInfoRepository(session);

    @Test
    void whenSaveThenReturnEntity() {
        session.beginTransaction();

        UserInfo givenUserInfo = TestEntity.getTestUserInfo();
        UserInfo savedUserInfo = repository.save(givenUserInfo);
        session.flush();
        session.clear();
        UserInfo actualUserInfo = session.get(UserInfo.class, savedUserInfo.getId());

        assertThat(actualUserInfo.getId()).isNotNull();

        session.getTransaction().rollback();
    }

    @Test
    void whenDeleteThenOk() {
        session.beginTransaction();

        UserInfo givenUserInfo = TestEntity.getTestUserInfo();
        Serializable savedId = session.save(givenUserInfo);
        session.flush();
        session.clear();
        UserInfo actualUserInfo = session.get(UserInfo.class, savedId);
        repository.delete(actualUserInfo);
        session.clear();
        UserInfo deletedUserInfo = session.get(UserInfo.class, savedId);

        assertThat(deletedUserInfo).isNull();

        session.getTransaction().rollback();
    }

    @Test
    void whenUpdateThenReturnUpdatedEntity() {
        session.beginTransaction();

        UserInfo givenUserInfo = TestEntity.getTestUserInfo();
        Serializable savedId = session.save(givenUserInfo);
        session.flush();
        session.clear();
        UserInfo actualUserInfo = session.get(UserInfo.class, savedId);
        actualUserInfo.setEmail("test@gmail.com");
        actualUserInfo.setUserAddress(UserAddress.builder()
                .town("Brest")
                .street("Kolasa")
                .houseNumber(15)
                .houseNumber(3)
                .postalCode(224000)
                .build());
        repository.update(actualUserInfo);
        session.flush();
        session.clear();
        session.get(UserInfo.class, savedId);

        assertThat(actualUserInfo.getEmail()).isEqualTo("test@gmail.com");
        assertThat(actualUserInfo.getUserAddress().getTown()).isEqualTo("Brest");

        session.getTransaction().rollback();
    }

    @Test
    void whenFindByIdWhenReturnEntity() {
        session.beginTransaction();

        UserInfo givenUserInfo = TestEntity.getTestUserInfo();
        Serializable savedId = session.save(givenUserInfo);
        session.flush();
        session.clear();
        Optional<UserInfo> actualUserInfo = repository.findById((Integer) savedId);

        assertThat(actualUserInfo).isPresent();
        actualUserInfo.ifPresent(userInfo->assertThat(userInfo.getId()).isEqualTo(savedId));

        session.getTransaction().rollback();
    }

    @Test
    void whenFindByWrongIdWhenNotFound() {
        session.beginTransaction();

        UserInfo givenUserInfo = TestEntity.getTestUserInfo();
        session.save(givenUserInfo);
        session.flush();
        session.clear();
        Optional<UserInfo> maybeUserInfo = repository.findById(Integer.MAX_VALUE);

        assertThat(maybeUserInfo).isEmpty();

        session.getTransaction().rollback();
    }

    @Test
    void whenFindAllThenReturnListEntities() {
        session.beginTransaction();

        UserInfo givenUserInfo = TestEntity.getTestUserInfo();
        session.save(givenUserInfo);
        session.flush();
        session.clear();
        List<UserInfo> actual = repository.findAll();

        assertThat(actual.size()).isEqualTo(1);

        session.getTransaction().rollback();
    }
}