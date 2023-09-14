CREATE TABLE refresh_token
(
    id          BIGINT AUTO_INCREMENT,
    expire_date TIMESTAMP(6) NOT NULL,
    token       TEXT         NOT NULL,
    created_at  TIMESTAMP(6) NOT NULL,
    updated_at  TIMESTAMP(6) NOT NULL,
    deleted_at  TIMESTAMP(6),
    member_id   BIGINT       NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

ALTER TABLE refresh_token
    ADD CONSTRAINT fk_refresh_token_to_member
        FOREIGN KEY (member_id) REFERENCES member (id);
