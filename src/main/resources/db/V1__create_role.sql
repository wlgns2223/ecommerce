CREATE TABLE roles
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_code  VARCHAR(255) NOT NULL,
    role_name  VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_role_code UNIQUE (role_code)
);

INSERT INTO roles (role_code, role_name)
VALUES ('USER', '사용자');

INSERT INTO roles (role_code, role_name)
VALUES ('ADMIN', '관리자');