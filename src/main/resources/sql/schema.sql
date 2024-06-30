
drop table if exists users_positions;
drop table if exists positions;
drop table if exists users;
drop table if exists companies;

CREATE TABLE IF NOT EXISTS companies
(
    company_id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    company_name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users
(
    user_id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_firstname VARCHAR(255) NOT NULL,
    user_lastname  VARCHAR(255) NOT NULL,
    company_id     BIGINT REFERENCES companies (company_id)
);

CREATE TABLE IF NOT EXISTS positions
(
    position_id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    position_name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users_positions
(
    users_positions_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id            BIGINT REFERENCES users (user_id),
    position_id        BIGINT REFERENCES positions (position_id),
    CONSTRAINT unique_link UNIQUE (user_id, position_id)
);

INSERT INTO companies (company_name)
VALUES ('Aston'),
       ('Yandex'),
       ('Google');

INSERT INTO users (user_firstname, user_lastname, company_id)
VALUES ('Иван', 'Иванов', 1),
       ('Петр', 'Петров', 2),
       ('Алексей', 'Сидоров',2),
       ('Василий', 'Васильев', 3);

INSERT INTO positions (position_name)
VALUES ('Разработчик'),
       ('Администратор'),
       ('Бухгалтер'),
       ('Директор');

INSERT INTO users_positions (user_id, position_id)
 VALUES (1,1),
        (2,2),
        (3,4),
        (3,3);
