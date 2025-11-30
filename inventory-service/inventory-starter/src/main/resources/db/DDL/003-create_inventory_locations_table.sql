SET search_path TO "inventory";

DROP TABLE IF EXISTS "inventory".inventory_locations CASCADE;

CREATE TABLE "inventory".inventory_locations
(
    id                UUID    NOT NULL,
    inventory_item_id UUID    NOT NULL,
    warehouse_id      UUID    NOT NULL,
    aisle             VARCHAR(5),
    shelf             VARCHAR(10),
    is_incoming       BOOLEAN NOT NULL,
    quantity          NUMERIC NOT NULL,

    CONSTRAINT products_pk PRIMARY KEY (id)
);

ALTER TABLE "inventory".inventories
    ADD CONSTRAINT fk_inventory_location_inventory
        FOREIGN KEY (inventory_item_id)
            REFERENCES "inventory".inventories (id);