package com.andev.entity;

import com.andev.entity.enums.Role;
import com.andev.util.HibernateTestUtil;
import com.andev.util.TestEntity;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
    private static final SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();

    @Test
    void whenSave_thenSaveCorrect() {
        User givenUser = TestEntity.getTestUser();

        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Serializable savedUserId = session.save(givenUser);
        session.clear();
        User actualUser = session.get(User.class, savedUserId);

        assertThat(actualUser).isNotNull();

        session.getTransaction().rollback();
    }

    @Test
    void whenFindById_thenReturnEntity() {
        User givenUser = TestEntity.getTestUser();

        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Serializable savedUserId = session.save(givenUser);
        session.clear();
        User actualUser = session.get(User.class, savedUserId);

        assertThat(actualUser.getId()).isEqualTo(savedUserId);

        session.getTransaction().rollback();
    }

    @Test
    void whenFindById_thenNotFound() {
        User givenUser = TestEntity.getTestUser();

        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        session.save(givenUser);
        session.clear();
        User maybeUser = session.get(User.class, Integer.MAX_VALUE);

        assertThat(maybeUser).isNull();

        session.getTransaction().rollback();
    }

    @Test
    void whenUpdate_thenOk() {
        User givenUser = TestEntity.getTestUser();

        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Serializable savedUserId = session.save(givenUser);
        session.clear();
        User actualUser = session.get(User.class, savedUserId);
        actualUser.setRole(Role.ADMIN);
        session.update(actualUser);
        session.flush();
        session.clear();
        User updatedUser = session.get(User.class, savedUserId);

        assertThat(updatedUser.getRole()).isEqualTo(Role.ADMIN);

        session.getTransaction().rollback();
    }

    @Test
    void whenDelete_thenOk() {
        User givenUser = TestEntity.getTestUser();

        @Cleanup Session session = sessionFactory.openSession();
        session.getTransaction().begin();
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