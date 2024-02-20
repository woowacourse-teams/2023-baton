package touch.baton.common.schedule.deadline.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RunnerPostDeadlineSchedulerService {

    private static final int CYCLE = 60000;

    private final RunnerPostDeadlineCheckScheduler runnerPostDeadlineCheckScheduler;

    @Scheduled(fixedRate = CYCLE)
    public void updateReviewStatus() {
        runnerPostDeadlineCheckScheduler.updateReviewStatus();
    }
}
