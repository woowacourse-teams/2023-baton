package touch.baton.domain.feedback.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.feedback.command.SupporterFeedback;

public interface SupporterFeedbackCommandRepository extends JpaRepository<SupporterFeedback, Long> {

    boolean existsByRunnerPostIdAndSupporterId(final Long runnerPostId, final Long supporterId);
}
