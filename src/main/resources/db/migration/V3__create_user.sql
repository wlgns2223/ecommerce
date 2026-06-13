CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    nickname   VARCHAR(255) NOT NULL,
    phone      VARCHAR(255) NOT NULL,
    status     VARCHAR(255) NOT NULL,
    role       VARCHAR(255) NOT NULL DEFAULT 'USER',
    detail_id  BIGINT       NOT NULL,
    created_at TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_email UNIQUE (email),
    CONSTRAINT fk_user_detail_id FOREIGN KEY (detail_id) REFERENCES user_details (id)
);