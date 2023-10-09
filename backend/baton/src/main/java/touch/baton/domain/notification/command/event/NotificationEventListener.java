package touch.baton.domain.notification.command.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import touch.baton.domain.notification.command.Notification;
import touch.baton.domain.notification.command.repository.NotificationCommandRepository;
import touch.baton.domain.notification.command.vo.NotificationMessage;
import touch.baton.domain.notification.command.vo.NotificationReferencedId;
import touch.baton.domain.notification.command.vo.NotificationText;
import touch.baton.domain.notification.command.vo.NotificationTitle;
import touch.baton.domain.notification.command.vo.NotificationType;
import touch.baton.domain.notification.command.vo.IsRead;
import touch.baton.domain.notification.exception.NotificationBusinessException;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.event.RunnerPostApplySupporterEvent;
import touch.baton.domain.runnerpost.command.event.RunnerPostAssignSupporterEvent;
import touch.baton.domain.runnerpost.command.event.RunnerPostReviewStatusDoneEvent;
import touch.baton.domain.runnerpost.query.repository.RunnerPostQueryRepository;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;
import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;
import static touch.baton.domain.notification.command.vo.NotificationText.MESSAGE_REFERENCED_BY_RUNNER_POST;
import static touch.baton.domain.notification.command.vo.NotificationText.TITLE_RUNNER_POST_APPLICANT;
import static touch.baton.domain.notification.command.vo.NotificationText.TITLE_RUNNER_POST_ASSIGN_SUPPORTER;
import static touch.baton.domain.notification.command.vo.NotificationText.TITLE_RUNNER_POST_REVIEW_STATUS_DONE;

@RequiredArgsConstructor
@Component
public class NotificationEventListener {

    private final NotificationCommandRepository notificationCommandRepository;
    private final RunnerPostQueryRepository runnerPostQueryRepository;

    @TransactionalEventListener(phase = AFTER_COMMIT)
    @Transactional(propagation = REQUIRES_NEW)
    public void subscribeRunnerPostApplySupporterEvent(final RunnerPostApplySupporterEvent event) {
        final RunnerPost foundRunnerPost = getRunnerPostWithRunnerOrThrowException(event.runnerPostId());

        notificationCommandRepository.save(
                createNotification(TITLE_RUNNER_POST_APPLICANT, foundRunnerPost, foundRunnerPost.getRunner().getMember())
        );
    }

    private RunnerPost getRunnerPostWithRunnerOrThrowException(final Long runnerPostId) {
        return runnerPostQueryRepository.joinMemberByRunnerPostId(runnerPostId)
                .orElseThrow(() -> new NotificationBusinessException("러너 게시글 식별자값으로 러너 게시글과 러너(작성자)를 조회하던 도중에 오류가 발생하였습니다."));
    }

    private Notification createNotification(final NotificationText notificationText, final RunnerPost runnerPost, final Member targetMember) {
        return Notification.builder()
                .notificationTitle(new NotificationTitle(notificationText.getText()))
                .notificationMessage(new NotificationMessage(String.format(MESSAGE_REFERENCED_BY_RUNNER_POST.getText(), runnerPost.getTitle().getValue())))
                .notificationType(NotificationType.RUNNER_POST)
                .notificationReferencedId(new NotificationReferencedId(runnerPost.getId()))
                .isRead(IsRead.asUnRead())
                .member(targetMember)
                .build();
    }

    @TransactionalEventListener(phase = AFTER_COMMIT)
    @Transactional(propagation = REQUIRES_NEW)
    public void subscribeRunnerPostReviewStatusDoneEvent(final RunnerPostReviewStatusDoneEvent event) {
        final RunnerPost foundRunnerPost = getRunnerPostWithRunnerOrThrowException(event.runnerPostId());

        notificationCommandRepository.save(
                createNotification(TITLE_RUNNER_POST_REVIEW_STATUS_DONE, foundRunnerPost, foundRunnerPost.getRunner().getMember())
        );
    }

    @TransactionalEventListener(phase = AFTER_COMMIT)
    @Transactional(propagation = REQUIRES_NEW)
    public void subscribeRunnerPostAssignSupporterEvent(final RunnerPostAssignSupporterEvent event) {
        final RunnerPost foundRunnerPost = getRunnerPostWithSupporterOrThrowException(event.runnerPostId());

        notificationCommandRepository.save(
                createNotification(TITLE_RUNNER_POST_ASSIGN_SUPPORTER, foundRunnerPost, foundRunnerPost.getSupporter().getMember())
        );
    }

    private RunnerPost getRunnerPostWithSupporterOrThrowException(final Long runnerPostId) {
        return runnerPostQueryRepository.joinSupporterByRunnerPostId(runnerPostId)
                .orElseThrow(() -> new NotificationBusinessException("러너 게시글 식별자값으로 러너 게시글과 서포터(지원자)를 조회하던 도중에 오류가 발생하였습니다."));
    }
}
