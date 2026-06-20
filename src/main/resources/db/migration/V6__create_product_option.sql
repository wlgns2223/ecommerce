CREATE TABLE product_option_groups
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT       NOT NULL,
    name       VARCHAR(255) NOT NULL CHECK ( TRIM(name) <> ''),
    sort_order INT          NOT NULL DEFAULT 0 CHECK ( sort_order >= 0),
    deleted_at TIMESTAMP    NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_option_group_prod_id FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE TABLE product_option_values
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    option_group_id BIGINT       NOT NULL,
    value           VARCHAR(255) NOT NULL CHECK ( TRIM(value) <> ''),
    sort_order      INT          NOT NULL DEFAULT 0 CHECK ( sort_order >= 0),
    deleted_at      TIMESTAMP    NULL,
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_option_group_id FOREIGN KEY (option_group_id) REFERENCES product_option_groups (id)
);