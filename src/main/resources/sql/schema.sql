
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
    user_id              BIGINT REFERENCES users (user_id),
    position_id        BIGINT REFERENCES positions (position_id),
    CONSTRAINT unique_link UNIQUE (user_id, position_id)
);