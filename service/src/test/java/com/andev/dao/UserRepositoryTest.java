package com.andev.dao;

import com.andev.entity.User;
import com.andev.entity.enums.Role;
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

class UserRepositoryTest {
    private final SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();
    private final Session session = ProxySession.makeProxy(sessionFactory);
    private final UserRepository repository = new UserRepository(session);

    @Test
    void whenSaveThenReturnEntity() {
        session.beginTransaction();

        User givenUser = TestEntity.getTestUser();
        User savedUser = repository.save(givenUser);
        session.flush();
        session.clear();
        User actualUser = session.get(User.class, savedUser.getId());

        assertThat(actualUser.getId()).isNotNull();

        session.getTransaction().rollback();
    }

    @Test
    void whenDeleteThenOk() {
        session.beginTransaction();

        User givenUser = TestEntity.getTestUser();
        Serializable savedId = session.save(givenUser);
        session.flush();
        session.clear();
        User actualUser = session.get(User.class, savedId);
        repository.delete(actualUser);
        session.clear();
        User deletedUser = session.get(User.class, savedId);

        assertThat(deletedUser).isNull();

        session.getTransaction().rollback();
    }

    @Test
    void whenUpdateThenReturnUpdatedEntity() {
        session.beginTransaction();

        User givenUser = TestEntity.getTestUser();
        Serializable savedId = session.save(givenUser);
        session.flush();
        session.clear();
        User actualUser = session.get(User.class, savedId);
        actualUser.setRole(Role.ADMIN);
        repository.update(actualUser);
        session.flush();
        session.clear();
        session.get(User.class, savedId);

        assertThat(actualUser.getRole()).isEqualTo(Role.ADMIN);

        session.getTransaction().rollback();
    }

    @Test
    void whenFindByIdWhenReturnEntity() {
        session.beginTransaction();

        User givenUser = TestEntity.getTestUser();
        Serializable savedId = session.save(givenUser);
        session.flush();
        session.clear();
        Optional<User> actualUser = repository.findById((Integer) savedId);

        assertThat(actualUser).isPresent();
        actualUser.ifPresent(u->assertThat(u.getId()).isEqualTo(savedId));

        session.getTransaction().rollback();
    }

    @Test
    void whenFindByWrongIdWhenNotFound() {
        session.beginTransaction();

        User givenUser = TestEntity.getTestUser();
        session.save(givenUser);
        session.flush();
        session.clear();
        Optional<User> maybeUser = repository.findById(Integer.MAX_VALUE);

        assertThat(maybeUser).isEmpty();

        session.getTransaction().rollback();
    }

    @Test
    void whenFindAllThenReturnListEntities() {
        session.beginTransaction();

        User givenUser = TestEntity.getTestUser();
        session.save(givenUser);
        session.flush();
        session.clear();
        List<User> actual = repository.findAll();

        assertThat(actual.size()).isEqualTo(1);

        session.getTransaction().rollback();
    }
}