CREATE TABLE user_details
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    gender       VARCHAR(255) NOT NULL,
    birth_date   DATETIME     NOT NULL,
    role_id      BIGINT       NOT NULL,
    user_id      BIGINT       NOT NULL,
    user_tier_id BIGINT       NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_tier FOREIGN KEY (user_tier_id) REFERENCES user_tiers (id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_role_id FOREIGN KEY (role_id) REFERENCES roles (id)
);