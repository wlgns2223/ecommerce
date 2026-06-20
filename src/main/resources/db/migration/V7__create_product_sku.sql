CREATE TABLE product_skus
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    sku_code   VARCHAR(255)   NOT NULL CHECK ( TRIM(sku_code) <> ''),
    price      DECIMAL(10, 2) NULL,
    stock      INT            NOT NULL DEFAULT 0 CHECK ( stock >= 0 ),
    product_id BIGINT         NOT NULL,
    deleted_at TIMESTAMP      NULL,
    created_at TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_sku_product_id FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT uk_product_sku_code UNIQUE (sku_code)
);

CREATE TABLE sku_option_values
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    sku_id          BIGINT    NOT NULL,
    option_value_id BIGINT    NOT NULL,
    deleted_at      TIMESTAMP NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    active_flag     TINYINT(1) as (IF(deleted_at IS NULL, 1, NULL)) VIRTUAL,
    CONSTRAINT fk_sku_option_value_sku_id FOREIGN KEY (sku_id) REFERENCES product_skus (id),
    CONSTRAINT fk_sku_option_value_option_value_id FOREIGN KEY (option_value_id) REFERENCES product_option_values (id),

    # 같은 옵션은 막을 수 있지만, 같은 카테고리의 서로 다른 옵션은 막을 수는 없음. 그러나 같은 옵션 카테고리가 들어가면 안됌
    # 도메인 로직으로 해결
    CONSTRAINT uk_sku_option_value_sku_option_value UNIQUE (sku_id, option_value_id, active_flag)
)