package com.andev.entity;

import com.andev.IntegrationTestBase;
import com.andev.entity.enumred.Status;
import com.andev.util.HibernateUtil;
import org.assertj.core.api.Assertions;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

class OrderIT extends IntegrationTestBase {

    @Test
    void whenSave_thenSaveCorrect() {
        Order given = Order.builder()
                .dateOrder(LocalDate.of(2022,9,23))
                .dateClosing(LocalDate.of(2022,9,27))
                .status(Status.ARRIVED)
                .build();
        Order expected = Order.builder()
                .id(2)
                .dateOrder(LocalDate.of(2022,9,23))
                .dateClosing(LocalDate.of(2022,9,27))
                .status(Status.ARRIVED)
                .build();
        Integer givenId = 2;
        Order actual;

        try (Session session = HibernateUtil.buildSession()) {
            session.getTransaction().begin();
            session.save(given);
            actual = session.get(Order.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actual).isEqualTo(expected);
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
        Order actual;

        try (Session session = HibernateUtil.buildSession()) {
            session.getTransaction().begin();
            actual = session.get(Order.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actual.getId()).isEqualTo(expectedId);
    }

    @Test
    void whenFindById_thenNotFound() {
        Integer givenId = Integer.MAX_VALUE;
        Order actual;

        try (Session session = HibernateUtil.buildSession()) {
            session.getTransaction().begin();
            actual = session.get(Order.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actual).isNull();
    }

    @Test
    void whenUpdate_thenOk() {
        Integer givenId = 1;
        Order given = Order.builder()
                .id(1)
                .dateOrder(LocalDate.of(2020,4,1))
                .dateClosing(LocalDate.of(2020,4,3))
                .status(Status.SENT)
                .build();
        Status expectedStatus = Status.SENT;
        Order actual;

        try (Session session = HibernateUtil.buildSession()) {
            session.getTransaction().begin();
            session.update(given);
            session.flush();
            session.clear();
            actual = session.get(Order.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actual.getStatus()).isEqualTo(expectedStatus);
    }

    @Test
    void whenDelete_thenOk() {
        Integer givenId = 1;
        Order given = Order.builder()
                .id(1)
                .build();
        Order actual;

        try (Session session = HibernateUtil.buildSession()) {
            session.getTransaction().begin();
            session.delete(given);
            session.flush();
            session.clear();
            actual = session.get(Order.class, givenId);
            session.getTransaction().commit();
        }

        Assertions.assertThat(actual).isNull();
    }

}