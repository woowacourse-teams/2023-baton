package touch.baton.domain.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.feedback.SupporterFeedback;

public interface SupporterFeedbackRepository extends JpaRepository<SupporterFeedback, Long> {
}
