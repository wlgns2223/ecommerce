CREATE TABLE categories
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(255) NOT NULL,
    parent_id  BIGINT       NOT NULL,
    sort_order INT          NOT NULL DEFAULT 0,
    depth      INT          NOT NULL DEFAULT 0,
    slug       VARCHAR(64)  NOT NULL,
    is_active  BOOLEAN      NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP    NULL     DEFAULT NULL,
    CONSTRAINT fk_parent_id FOREIGN KEY (parent_id) REFERENCES categories (id),
    CONSTRAINT uk_slug UNIQUE (parent_id, slug),
    CONSTRAINT uk_name UNIQUE (parent_id, name)
)