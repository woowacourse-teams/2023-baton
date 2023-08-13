package touch.baton.domain.runnerpost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.supporter.SupporterRunnerPost;

public interface SupporterRunnerPostRepository extends JpaRepository<SupporterRunnerPost, Long> {

    boolean existsByRunnerPostIdAndSupporterId(final Long runnerPostId, final Long supporterId);
}
