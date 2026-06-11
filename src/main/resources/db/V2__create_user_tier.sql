CREATE TABLE user_tiers
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    tier_code  VARCHAR(255) NOT NULL,
    tier_name  VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_tier_code UNIQUE (tier_code)
);

INSERT INTO user_tiers (tier_code, tier_name)
VALUES ('basic', '일반');
INSERT INTO user_tiers (tier_code, tier_name)
VALUES ('silver', '실버');
INSERT INTO user_tiers (tier_code, tier_name)
VALUES ('gold', '골드');
INSERT INTO user_tiers (tier_code, tier_name)
VALUES ('platinum', '플래티넘');