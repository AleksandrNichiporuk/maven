package com.andev.entity;

import com.andev.util.HibernateTestUtil;
import com.andev.util.TestEntity;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.assertj.core.api.Assertions.assertThat;

class UserInfoTest {
    private static final SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();

    @Test
    void whenSave_thenSaveCorrect() {
        User givenUser = TestEntity.getTestUser();
        UserInfo givenUserInfo = TestEntity.getTestUserInfo();

        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        givenUserInfo.addUser(givenUser);
        Serializable savedUserId = session.save(givenUser);
        session.clear();
        User actualUser = session.get(User.class, savedUserId);

        assertThat(actualUser.getUserInfo()).isNotNull();

        session.getTransaction().rollback();

    }

    @Test
    void whenFindById_thenReturnEntity() {
        User givenUser = TestEntity.getTestUser();
        UserInfo givenUserInfo = TestEntity.getTestUserInfo();

        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        givenUserInfo.addUser(givenUser);
        Serializable savedUserId = session.save(givenUser);
        session.clear();
        User actualUser = session.get(User.class, savedUserId);

        assertThat(actualUser.getUserInfo().getId()).isEqualTo(givenUser.getUserInfo().getId());

        session.getTransaction().rollback();
    }

    @Test
    void whenFindById_thenNotFound() {
        User givenUser = TestEntity.getTestUser();
        UserInfo givenUserInfo = TestEntity.getTestUserInfo();

        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        givenUserInfo.addUser(givenUser);
        session.save(givenUser);
        session.clear();
        User maybeUser = session.get(User.class, Integer.MAX_VALUE);

        assertThat(maybeUser).isNull();

        session.getTransaction().rollback();
    }

    @Test
    void whenUpdate_thenOk() {
        User givenUser = TestEntity.getTestUser();
        UserInfo givenUserInfo = TestEntity.getTestUserInfo();

        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        givenUserInfo.addUser(givenUser);
        Serializable savedUserId = session.save(givenUser);
        session.clear();
        User actualUser = session.get(User.class, savedUserId);
        actualUser.getUserInfo().setEmail("test@gmail.com");
        actualUser.getUserInfo().setUserAddress(UserAddress.builder()
                .town("Brest")
                .street("Kolasa")
                .houseNumber(15)
                .houseNumber(3)
                .postalCode(224000)
                .build());
        session.update(actualUser);
        session.flush();
        session.clear();
        User updatedUser = session.get(User.class, savedUserId);

        assertThat(updatedUser.getUserInfo().getEmail()).isEqualTo("test@gmail.com");
        assertThat(updatedUser.getUserInfo().getUserAddress().getTown()).isEqualTo("Brest");

        session.getTransaction().rollback();
    }

    @Test
    void whenDelete_thenOk() {
        User givenUser = TestEntity.getTestUser();
        UserInfo givenUserInfo = TestEntity.getTestUserInfo();

        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        givenUserInfo.addUser(givenUser);
        Serializable savedUserId = session.save(givenUser);
        session.clear();
        User actualUser = session.get(User.class, savedUserId);
        session.delete(actualUser);
        session.flush();
        User deletedUser = session.get(User.class, savedUserId);

        assertThat(deletedUser).isNull();

        session.getTransaction().rollback();
    }
}