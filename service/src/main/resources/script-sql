CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    login    VARCHAR(128) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL
);



CREATE TABLE user_info
(
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(128),
    last_name  VARCHAR(128),
    email      VARCHAR(128),
    phone      VARCHAR(16),
    user_id    INT REFERENCES users (id)
);

CREATE TABLE role
(
    id        SERIAL PRIMARY KEY,
    role_enum VARCHAR(64),
    user_id   INT REFERENCES users (id)
);

CREATE TABLE orders
(
    id           SERIAL PRIMARY KEY,
    date_order   DATE,
    date_closing DATE,
    status       VARCHAR(64),
    user_id      INT REFERENCES users (id)
);

CREATE TABLE product
(
    id           SERIAL PRIMARY KEY,
    name_product VARCHAR(256),
    description  TEXT,
    price        DECIMAL,
    amount       INT,
    order_id     INT REFERENCES orders (id)
);

CREATE TABLE order_product
(
    order_id   INT REFERENCES orders (id),
    product_id INT REFERENCES product (id)
);
