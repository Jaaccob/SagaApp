SET search_path TO "inventory";

DROP TABLE IF EXISTS "inventory".inventories CASCADE;

CREATE TABLE "inventory".inventories
(
    id                 UUID         NOT NULL,
    product_id         UUID         NOT NULL,
    code               VARCHAR(255) NOT NULL,
    quantity_total     NUMERIC      NOT NULL,
    quantity_reserved  NUMERIC      NOT NULL,
    quantity_available NUMERIC      NOT NULL,
    CONSTRAINT products_pk PRIMARY KEY (id)
);

ALTER TABLE "inventory".inventories
    ADD CONSTRAINT fk_inventory_product
        FOREIGN KEY (product_id)
            REFERENCES "product".products (id);