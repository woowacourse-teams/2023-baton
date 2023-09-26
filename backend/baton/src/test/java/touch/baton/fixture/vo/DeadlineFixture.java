package touch.baton.fixture.vo;

import touch.baton.tobe.domain.runnerpost.command.vo.Deadline;

import java.time.LocalDateTime;

public abstract class DeadlineFixture {

    private DeadlineFixture() {
    }

    public static Deadline deadline(final LocalDateTime value) {
        return new Deadline(value);
    }
}
