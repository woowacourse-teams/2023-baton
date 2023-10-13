CREATE TABLE notification
(
    id                BIGINT            NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title             VARCHAR(255)      NOT NULL,
    message           VARCHAR(255)      NOT NULL,
    notification_type VARCHAR(255)      NOT NULL,
    is_read           bit default false NOT NULL,
    referenced_id     BIGINT            NOT NULL,
    member_id         BIGINT            NOT NULL,
    created_at        DATETIME(6)       NOT NULL,
    updated_at        DATETIME(6)       NOT NULL,
    deleted_at        DATETIME(6)
);
