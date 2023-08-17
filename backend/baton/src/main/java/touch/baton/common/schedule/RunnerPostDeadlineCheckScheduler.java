package touch.baton.common.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class RunnerPostDeadlineCheckScheduler {

    private final ScheduleRunnerPostRepository scheduleRunnerPostRepository;

    @Transactional
    public void updateReviewStatus() {
        scheduleRunnerPostRepository.updateAllPassedDeadline();
    }
}
