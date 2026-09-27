CREATE TABLE carts
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    deleted_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT uk_cart_users UNIQUE (user_id)
);

CREATE TABLE cart_items
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id    BIGINT         NOT NULL,
    sku_id     BIGINT         NOT NULL,
    quantity   INT UNSIGNED   NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    deleted_at TIMESTAMP,
    created_at TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cart_id) REFERENCES carts (id) ON DELETE CASCADE,
    FOREIGN KEY (sku_id) REFERENCES product_skus (id),
    CONSTRAINT uk_cart_items UNIQUE (cart_id, sku_id)
);