package touch.baton.common.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SchedulerService {

    private static final int CYCLE = 6000;

    private final RunnerPostDeadlineCheckScheduler runnerPostDeadlineCheckScheduler;

    @Scheduled(fixedRate = CYCLE)
    public void updateReviewStatus() {
        runnerPostDeadlineCheckScheduler.updateReviewStatus();
    }
}
