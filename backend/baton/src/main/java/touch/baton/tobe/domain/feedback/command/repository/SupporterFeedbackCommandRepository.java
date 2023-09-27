package touch.baton.tobe.domain.feedback.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.tobe.domain.feedback.command.SupporterFeedback;

public interface SupporterFeedbackCommandRepository extends JpaRepository<SupporterFeedback, Long> {

    boolean existsByRunnerPostIdAndSupporterId(final Long runnerPostId, final Long supporterId);
}
