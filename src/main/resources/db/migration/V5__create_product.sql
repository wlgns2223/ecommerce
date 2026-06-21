CREATE TABLE products
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(255)   NOT NULL CHECK ( TRIM(name) <> ''),
    #판매가
    sale_price    DECIMAL(10, 2) NOT NULL DEFAULT 0 CHECK ( sale_price >= 0 ),

    #원가
    retail_price  DECIMAL(10, 2) NOT NULL DEFAULT 0 CHECK ( retail_price >= 0 ),
    description   TEXT           NULL,
    model_number  VARCHAR(255)   NOT NULL CHECK ( TRIM(model_number) <> '' ),
    seller_id     BIGINT         NOT NULL,
    sale_unit     VARCHAR(255)   NULL, # 판매단위 표시
    min_order_qty INT            NOT NULL DEFAULT 1,
    max_order_qty INT            NOT NULL DEFAULT 9999,
    order_unit    INT            NOT NULL DEFAULT 1 CHECK ( order_unit >= 1 ),
    status        VARCHAR(255)   NOT NULL DEFAULT 'DRAFT' CHECK ( status IN ('DRAFT', 'ON_SALE', 'SUSPENDED', 'RESERVED', 'DISCONTINUED')),
    deleted_at    TIMESTAMP      NULL,
    created_at    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_seller_id FOREIGN KEY (seller_id) REFERENCES users (id)
)