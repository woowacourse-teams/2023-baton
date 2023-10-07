ALTER TABLE alarm
    ADD CONSTRAINT fk_alarm_to_member
        FOREIGN KEY (member_id) REFERENCES member (id);
