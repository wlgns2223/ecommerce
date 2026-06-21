CREATE TABLE categories
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    parent_id   BIGINT       NOT NULL DEFAULT 0, # 최상위 카테고리는 root id가 0이다.
    slug        VARCHAR(64)  NOT NULL,
    is_active   BOOLEAN      NOT NULL DEFAULT TRUE,
    active_flag TINYINT(1) as (IF(deleted_at IS NULL, 1, NULL)) VIRTUAL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at  TIMESTAMP    NULL     DEFAULT NULL,
    CONSTRAINT fk_parent_id FOREIGN KEY (parent_id) REFERENCES categories (id),
    CONSTRAINT uk_slug UNIQUE (parent_id, slug, active_flag),
    CONSTRAINT uk_name UNIQUE (parent_id, name, active_flag)
)