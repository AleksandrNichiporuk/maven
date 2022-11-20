package com.andev;

import com.andev.util.HibernateUtil;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;

public abstract class IntegrationTestBase {

    private static final String CREATE_USER_TABLE = """
            CREATE TABLE users
            (
                id         SERIAL PRIMARY KEY,
                login      VARCHAR(128) NOT NULL UNIQUE ,
                password   VARCHAR(128) NOT NULL
            );
            """;

    private static final String CREATE_USER_INFO_TABLE = """
            CREATE TABLE user_info
            (
                   id SERIAL PRIMARY KEY,
                   first_name VARCHAR(128),
                   last_name VARCHAR(128),
                   email VARCHAR(128),
                   phone VARCHAR(16),
                   user_id INT REFERENCES users(id)
            );
            """;

    private static final String CREATE_ROLE_TABLE = """
            CREATE TABLE role
            (
                     id SERIAL PRIMARY KEY,
                     role_enum VARCHAR(64),
                     user_id INT REFERENCES users(id)
            );
            """;

    private static final String CREATE_ORDER_TABLE = """
            CREATE TABLE orders
                (
                    id           SERIAL PRIMARY KEY,
                    date_order   DATE,
                    date_closing DATE,
                    status       VARCHAR(64),
                    user_id      INT REFERENCES users (id)
                );
            """;

    private static final String CREATE_PRODUCT_TABLE = """
            CREATE TABLE product
                  (
                      id           SERIAL PRIMARY KEY,
                      name_product VARCHAR(256),
                      description  TEXT,
                      price        DECIMAL,
                      amount       INT,
                      order_id     INT REFERENCES orders (id)
                  );
            """;

    private static final String INSERT_USER = """
            INSERT INTO users (login, password)
            VALUES ('ivan', '123')
            """;

    private static final String INSERT_USER_INFO = """
            INSERT INTO user_info (first_name, last_name, email, phone)
            VALUES ('Ivan', 'Ivanov', 'ivanov@gmail.com','7777777')
            """;

    private static final String INSERT_ROLE = """
            INSERT INTO role (role_enum)
            VALUES ('ADMIN')
            """;

    private static final String INSERT_ORDER = """
            INSERT INTO orders (date_order, date_closing, status)
            VALUES ('2022-04-01', '2022-04-03', 'ARRIVED')
            """;

    private static final String INSERT_PRODUCT = """
            INSERT INTO product (name_product, description, price, amount)
            VALUES ('product', 'text', 21, 100)
            """;


    private static final String CLEAN_SQL = "DROP TABLE IF EXISTS %s CASCADE;";

    @BeforeEach
    @SneakyThrows
    void prepareDatabase() {
        try (Session session = HibernateUtil.buildSession()) {
            session.getTransaction().begin();

            session.createSQLQuery(CLEAN_SQL.formatted("users")).executeUpdate();
            session.createSQLQuery(CLEAN_SQL.formatted("user_info")).executeUpdate();
            session.createSQLQuery(CLEAN_SQL.formatted("role")).executeUpdate();
            session.createSQLQuery(CLEAN_SQL.formatted("orders")).executeUpdate();
            session.createSQLQuery(CLEAN_SQL.formatted("product")).executeUpdate();

            session.createSQLQuery(CREATE_USER_TABLE).executeUpdate();
            session.createSQLQuery(CREATE_USER_INFO_TABLE).executeUpdate();
            session.createSQLQuery(CREATE_ROLE_TABLE).executeUpdate();
            session.createSQLQuery(CREATE_ORDER_TABLE).executeUpdate();
            session.createSQLQuery(CREATE_PRODUCT_TABLE).executeUpdate();

            session.createSQLQuery(INSERT_USER).executeUpdate();
            session.createSQLQuery(INSERT_USER_INFO).executeUpdate();
            session.createSQLQuery(INSERT_ROLE).executeUpdate();
            session.createSQLQuery(INSERT_ORDER).executeUpdate();
            session.createSQLQuery(INSERT_PRODUCT).executeUpdate();

            session.getTransaction().commit();
        }
    }
}
