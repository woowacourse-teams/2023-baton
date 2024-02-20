package touch.baton.common.schedule.deadline.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.common.schedule.deadline.command.exception.ScheduleBusinessException;
import touch.baton.common.schedule.deadline.command.repository.DeadlineOutboxCommandRepository;
import touch.baton.common.schedule.deadline.command.repository.RunnerPostDeadlineCommandRepository;
import touch.baton.common.schedule.deadline.query.repository.RunnerPostDeadlineQueryRepository;
import touch.baton.domain.runnerpost.command.RunnerPost;

@RequiredArgsConstructor
@Transactional
@Component
public class RunnerPostDeadlineCheckScheduler {

    private final RunnerPostDeadlineCommandRepository runnerPostDeadlineCommandRepository;
    private final RunnerPostDeadlineQueryRepository runnerPostDeadlineQueryRepository;
    private final DeadlineOutboxCommandRepository deadlineOutboxCommandRepository;

    public void updateReviewStatus() {
        runnerPostDeadlineCommandRepository.updateAllPassedDeadline();
    }

    @Transactional(readOnly = true)
    public RunnerPost joinBySupporter(final Long runnerPostId) {
        return runnerPostDeadlineQueryRepository.joinSupporterByRunnerPostId(runnerPostId)
                .orElseThrow(() -> new ScheduleBusinessException(String.format("RunnerPost를 찾을 수 없습니다. runnerPostId=%s",runnerPostId)));
    }

    public void finishReview(final Long runnerPostId) {
        final RunnerPost findRunnerPost = runnerPostDeadlineQueryRepository.joinSupporterByRunnerPostIdWithLock(runnerPostId)
                .orElseThrow(() -> new ScheduleBusinessException(String.format("RunnerPost를 찾을 수 없습니다. runnerPostId=%s",runnerPostId)));
        if (findRunnerPost.isReviewStatusDone()) {
            return;
        }

        findRunnerPost.finishReview();
        deadlineOutboxCommandRepository.deleteByRunnerPostId(runnerPostId);
    }
}
