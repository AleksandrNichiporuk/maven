CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    login    VARCHAR(128) NOT NULL,
    password VARCHAR(128) NOT NULL,
    role     VARCHAR(64)  NOT NULL
);

CREATE TABLE user_info
(
    id               SERIAL PRIMARY KEY,
    first_name       VARCHAR(128) NOT NULL,
    last_name        VARCHAR(128) NOT NULL,
    email            VARCHAR(128) NOT NULL UNIQUE,
    phone            VARCHAR(16),
    town             VARCHAR(32),
    street           VARCHAR(32),
    house_number     INT,
    apartment_number INT,
    postal_code      INT,
    user_id          INT          NOT NULL UNIQUE REFERENCES users (id)
);

CREATE TABLE orders
(
    id           SERIAL PRIMARY KEY,
    date_order   DATE        NOT NULL,
    date_closing DATE,
    total_value  INT,
    payment      VARCHAR(16) NOT NULL,
    status       VARCHAR(64) NOT NULL,
    user_id      INT REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE manufacturer
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(64) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE product
(
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(64) NOT NULL,
    model           VARCHAR(16),
    category        VARCHAR(32),
    description     TEXT,
    price           DECIMAL     NOT NULL,
    amount          INT,
    available       BOOLEAN,
    manufacturer_id INT         NOT NULL UNIQUE REFERENCES manufacturer (id)
);

CREATE TABLE product_order
(
    product_id INT REFERENCES product (id) ON DELETE CASCADE,
    order_id   INT REFERENCES orders (id) ON DELETE CASCADE
);