package touch.baton.domain.supporter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.SupporterRunnerPost;

public interface SupporterRunnerPostRepository extends JpaRepository<SupporterRunnerPost, Long> {

    void deleteBySupporterAndRunnerPostId(final Supporter supporter, final Long runnerPostId);
}
