package touch.baton.common.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RunnerPostDeadlineCheckScheduler {

    private static final int CYCLE = 60000;

    private final ScheduleRunnerPostRepository scheduleRunnerPostRepository;

    @Scheduled(fixedRate = CYCLE)
    public void updateReviewStatus() {
        scheduleRunnerPostRepository.updateAllPassedDeadline();
    }
}
