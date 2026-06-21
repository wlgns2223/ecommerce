CREATE TABLE user_details
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    gender     VARCHAR(255) NOT NULL,
    birth_date DATETIME     NOT NULL,
    tier_code  VARCHAR(255) NOT NULL,
    deleted_at TIMESTAMP    NULL DEFAULT NULL,
    created_at TIMESTAMP         DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_tier FOREIGN KEY (tier_code) REFERENCES user_tiers (tier_code)
);