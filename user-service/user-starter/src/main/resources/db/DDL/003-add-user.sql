SET search_path TO "user";

DROP TABLE IF EXISTS "user".users CASCADE;

CREATE TABLE "user".users (
  id          UUID                                          NOT NULL,
  username    VARCHAR(255)                                  NOT NULL,
  password    VARCHAR(255)                                  NOT NULL,
  email       VARCHAR(255)                                  NOT NULL,
  CONSTRAINT users_pk PRIMARY KEY (id)
);

DROP TABLE IF EXISTS "user".roles CASCADE;

CREATE TABLE "user".roles (
  id        UUID            DEFAULT uuid_generate_v4()      NOT NULL,
  name      VARCHAR(255)                                    NOT NULL,
  CONSTRAINT roles_pk PRIMARY KEY (id)
);

DROP TABLE IF EXISTS "user".user_roles CASCADE;

CREATE TABLE "user".user_roles (
  user_id   UUID            NOT NULL,
  role_id   UUID            NOT NULL,
  CONSTRAINT users_fk FOREIGN KEY (user_id) REFERENCES "user".users (id),
  CONSTRAINT roles_fk FOREIGN KEY (role_id) REFERENCES "user".roles (id)
);