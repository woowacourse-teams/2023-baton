package touch.baton.domain.member.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.member.command.SupporterRunnerPost;

public interface SupporterRunnerPostCommandRepository extends JpaRepository<SupporterRunnerPost, Long> {

    boolean existsByRunnerPostId(final Long runnerPostId);

    boolean existsByRunnerPostIdAndSupporterId(final Long runnerPostId, final Long supporterId);

    void deleteBySupporterIdAndRunnerPostId(final Long supporterId, final Long runnerPostId);
}
