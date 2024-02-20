package touch.baton.common.schedule.deadline.command.event;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import touch.baton.common.schedule.deadline.command.service.RunnerPostDeadlineCheckScheduler;
import touch.baton.common.schedule.deadline.command.DeadlineOutbox;
import touch.baton.common.schedule.deadline.command.repository.DeadlineOutboxCommandRepository;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.event.RunnerPostApplySupporterEvent;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@RequiredArgsConstructor
public class TaskSchedulerEventListener {

    private static final int GRACE_PERIODS_DAYS = 3;

    private final TaskScheduler taskScheduler;
    private final RunnerPostDeadlineCheckScheduler runnerPostDeadlineCheckScheduler;
    private final DeadlineOutboxCommandRepository deadlineOutboxCommandRepository;

    @TransactionalEventListener(phase = AFTER_COMMIT)
    @Transactional
    public void subscribeAppliedRunnerPostDeadline(final RunnerPostApplySupporterEvent event) {
        final RunnerPost findRunnerPost = runnerPostDeadlineCheckScheduler.joinBySupporter(event.runnerPostId());
        final Instant instantToRun = calculateInstant(findRunnerPost);

        final DeadlineOutbox deadlineOutBox = DeadlineOutbox.builder()
                .runnerPostId(event.runnerPostId())
                .instantToRun(instantToRun)
                .build();
        deadlineOutboxCommandRepository.save(deadlineOutBox);
        taskScheduler.schedule(() -> runnerPostDeadlineCheckScheduler.finishReview(event.runnerPostId()), instantToRun);
    }

    private Instant calculateInstant(final RunnerPost findRunnerPost) {
        final LocalDateTime gracePeriods = findRunnerPost.getDeadline().getValue().plusDays(GRACE_PERIODS_DAYS);
        return gracePeriods.atZone(ZoneId.systemDefault()).toInstant();
    }

    @EventListener(ApplicationStartedEvent.class)
    public void setUpSchedules() {
        deadlineOutboxCommandRepository.findAll().forEach(deadlineOutbox ->
                taskScheduler.schedule(() -> runnerPostDeadlineCheckScheduler.finishReview(
                        deadlineOutbox.getRunnerPostId()), deadlineOutbox.getInstantToRun()
                ));
    }
}

