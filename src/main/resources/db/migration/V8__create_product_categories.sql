CREATE TABLE product_categories
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id   BIGINT    NOT NULL,
    category_id  BIGINT    NOT NULL,
    is_primary   BOOLEAN   NOT NULL DEFAULT FALSE,
    sort_order   INT       NOT NULL CHECK ( sort_order >= 0),
    deleted_at   TIMESTAMP NULL,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    active_flag  TINYINT(1) as (IF(deleted_at IS NULL, 1, NULL)) VIRTUAL,
    primary_flag BIGINT AS (IF(is_primary = TRUE AND deleted_at IS NULL, product_id, NULL)) VIRTUAL,
    CONSTRAINT fk_product_categories_product_id FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT fk_product_categories_category_id FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT uk_product_categories_product_id_category_id UNIQUE (product_id, category_id, active_flag),
    CONSTRAINT uk_product_categories_is_primary UNIQUE (primary_flag)
)