CREATE TABLE vehicle (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    plate      VARCHAR(7)   NOT NULL,
    category   VARCHAR(20)  NOT NULL,
    status     VARCHAR(20)  NOT NULL,
    mileage_km INT          NOT NULL DEFAULT 0,
    created_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT uk_vehicle_plate UNIQUE (plate),
    CONSTRAINT ck_vehicle_mileage CHECK (mileage_km >= 0)
);
