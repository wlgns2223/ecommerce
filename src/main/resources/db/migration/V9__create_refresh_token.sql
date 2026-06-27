CREATE TABLE refresh_tokens
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY ,
    user_id BIGINT NOT NULL ,
    token_hash VARCHAR(255) NOT NULL ,
    device_id VARCHAR(255) NOT NULL ,
    expires_at TIMESTAMP NOT NULL ,
    revoked_at TIMESTAMP NULL DEFAULT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_refresh_user_id FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT uk_refresh_token_hash UNIQUE (token_hash)
)