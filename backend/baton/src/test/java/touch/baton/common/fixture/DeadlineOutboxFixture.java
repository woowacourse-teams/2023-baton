package touch.baton.common.fixture;

import touch.baton.common.schedule.deadline.command.DeadlineOutbox;

import java.time.Instant;

public abstract class DeadlineOutboxFixture {

    private DeadlineOutboxFixture() {
    }

    public static DeadlineOutbox deadlineOutbox(final Long runnerPostId, final Instant instantToRun) {
        return DeadlineOutbox.builder()
                .runnerPostId(runnerPostId)
                .instantToRun(instantToRun)
                .build();
    }
}
