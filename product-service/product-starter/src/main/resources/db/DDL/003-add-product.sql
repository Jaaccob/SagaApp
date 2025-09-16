SET
search_path TO "product";

DROP TABLE IF EXISTS "product".products CASCADE;

CREATE TABLE "product".products
(
    id       UUID         NOT NULL,
    user_id   UUID         NOT NULL,
    code     VARCHAR(255) NOT NULL,
    name     VARCHAR(255) NOT NULL,
    price    NUMERIC      NOT NULL,
    quantity NUMERIC      NOT NULL,
    status   VARCHAR(255) NOT NULL,
    CONSTRAINT products_pk PRIMARY KEY (id)
);