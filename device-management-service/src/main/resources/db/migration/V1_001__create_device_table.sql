CREATE TABLE IF NOT EXISTS devices
(
    id                                BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id                            BIGINT       NOT NULL UNIQUE,
    description                       VARCHAR(255) NOT NULL,
    address                           VARCHAR(255) NOT NULL,
    maximum_hourly_energy_consumption INT          NOT NULL
);