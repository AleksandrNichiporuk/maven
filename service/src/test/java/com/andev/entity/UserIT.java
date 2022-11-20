package com.andev.entity;

import com.andev.IntegrationTestBase;
import com.andev.util.HibernateUtil;
import org.assertj.core.api.Assertions;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

class UserIT extends IntegrationTestBase {

    @Test
    void whenSave_thenSaveCorrect() {
        User givenUser = User.builder()
                .login("Petr")
                .password("333")
                .build();
        User expectedUser = User.builder()
                .id(2)
                .login("Petr")
                .password("333")
                .build();
        Integer givenId = 2;
        User actualUser;

        try (Session session = HibernateUtil.buildSession()) {
            session.getTransaction().begin();
            session.save(givenUser);
            actualUser = session.get(User.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actualUser).isEqualTo(expectedUser);
    }

    @Test
    void whenSave_thenTrowException() {
        User givenUser = User.builder().build();

        Throwable thrown = catchThrowable(() -> {
            try (Session session = HibernateUtil.buildSession()) {
                session.getTransaction().begin();
                session.save(givenUser);
                session.getTransaction().commit();
            }
        });

        Assertions.assertThat(thrown).isInstanceOf(RuntimeException.class);
    }

    @Test
    void whenFindById_thenReturnEntity() {
        Integer givenId = 1;
        Integer expectedId = 1;
        User actualUser;

        try (Session session = HibernateUtil.buildSession()) {
            session.getTransaction().begin();
            actualUser = session.get(User.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actualUser.getId()).isEqualTo(expectedId);
    }

    @Test
    void whenFindById_thenNotFound() {
        Integer givenId = Integer.MAX_VALUE;
        User actualUser;

        try (Session session = HibernateUtil.buildSession()) {
            session.getTransaction().begin();
            actualUser = session.get(User.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actualUser).isNull();
    }

    @Test
    void whenUpdate_thenOk() {
        Integer givenId = 1;
        User givenUser = User.builder()
                .id(1)
                .login("nikolai")
                .password("123456")
                .build();
        String expectedPassword = "123456";
        User actualUser;

        try (Session session = HibernateUtil.buildSession()) {
            session.getTransaction().begin();
            session.update(givenUser);
            session.flush();
            session.clear();
            actualUser = session.get(User.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actualUser.getPassword()).isEqualTo(expectedPassword);
    }

    @Test
    void whenDelete_thenOk() {
        Integer givenId = 1;
        User givenUser = User.builder()
                .id(1)
                .login("ivan")
                .password("123")
                .build();
        User actualUser;

        try (Session session = HibernateUtil.buildSession()) {
            session.getTransaction().begin();
            session.delete(givenUser);
            session.flush();
            session.clear();
            actualUser = session.get(User.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actualUser).isNull();
    }
}