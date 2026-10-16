CREATE TABLE customer (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    name           VARCHAR(120) NOT NULL,
    cpf            VARCHAR(11)  NOT NULL,
    birth_date     DATE         NOT NULL,
    email          VARCHAR(160) NULL,
    phone          VARCHAR(20)  NULL,
    cnh_number     VARCHAR(11)  NOT NULL,
    cnh_category   VARCHAR(3)   NOT NULL,
    cnh_expires_at DATE         NOT NULL,
    created_at     DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT uk_customer_cpf UNIQUE (cpf),
    CONSTRAINT uk_customer_cnh_number UNIQUE (cnh_number)
);
