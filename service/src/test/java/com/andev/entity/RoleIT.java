package com.andev.entity;

import com.andev.IntegrationTestBase;
import com.andev.entity.enumred.RoleEnum;
import com.andev.util.HibernateUtil;
import org.assertj.core.api.Assertions;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

class RoleIT extends IntegrationTestBase {

    @Test
    void whenSave_thenSaveCorrect() {
        Role givenRole = Role.builder()
                .roleEnum(RoleEnum.ADMIN)
                .build();
        Role expectedRole = Role.builder()
                .id(2)
                .roleEnum(RoleEnum.ADMIN)
                .build();
        Integer givenId = 2;
        Role actualRole;

        try (Session session = HibernateUtil.buildSession()) {
            session.getTransaction().begin();
            session.save(givenRole);
            actualRole = session.get(Role.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actualRole).isEqualTo(expectedRole);
    }

    @Test
    void whenSave_thenTrowException() {
        Throwable thrown = catchThrowable(() -> {
            try (Session session = HibernateUtil.buildSession()) {
                session.getTransaction().begin();
                session.save(null);
                session.getTransaction().commit();
            }
        });

        Assertions.assertThat(thrown).isInstanceOf(RuntimeException.class);
    }

    @Test
    void whenFindById_thenReturnEntity() {
        Integer givenId = 1;
        Integer expectedId = 1;
        Role actualRole;

        try (Session session = HibernateUtil.buildSession()) {
            session.getTransaction().begin();
            actualRole = session.get(Role.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actualRole.getId()).isEqualTo(expectedId);
    }

    @Test
    void whenFindById_thenNotFound() {
        Integer givenId = Integer.MAX_VALUE;
        Role actualRole;

        try (Session session = HibernateUtil.buildSession()) {
            session.getTransaction().begin();
            actualRole = session.get(Role.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actualRole).isNull();
    }

    @Test
    void whenUpdate_thenOk() {
        Integer givenId = 1;
        Role givenRole = Role.builder()
                .id(1)
                .roleEnum(RoleEnum.CUSTOMER)
                .build();
        RoleEnum expectedRole = RoleEnum.CUSTOMER;
        Role actualRole;

        try (Session session = HibernateUtil.buildSession()) {
            session.getTransaction().begin();
            session.update(givenRole);
            session.flush();
            session.clear();
            actualRole = session.get(Role.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actualRole.getRoleEnum()).isEqualTo(expectedRole);
    }

    @Test
    void whenDelete_thenOk() {
        Integer givenId = 1;
        Role givenRole = Role.builder()
                .id(1)
                .build();
        Role actualRole;

        try (Session session = HibernateUtil.buildSession()) {
            session.getTransaction().begin();
            session.delete(givenRole);
            session.flush();
            session.clear();
            actualRole = session.get(Role.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actualRole).isNull();
    }

}