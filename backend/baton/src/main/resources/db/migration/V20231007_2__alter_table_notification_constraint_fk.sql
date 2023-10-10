ALTER TABLE notification
    ADD CONSTRAINT fk_notification_to_member
        FOREIGN KEY (member_id) REFERENCES member (id);
