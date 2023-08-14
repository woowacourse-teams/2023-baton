package touch.baton.domain.supporter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.supporter.SupporterRunnerPost;

public interface SupporterRunnerPostRepository extends JpaRepository<SupporterRunnerPost, Long> {

    void deleteBySupporterIdAndRunnerPostId(final Long supporterId, final Long runnerPostId);
}
