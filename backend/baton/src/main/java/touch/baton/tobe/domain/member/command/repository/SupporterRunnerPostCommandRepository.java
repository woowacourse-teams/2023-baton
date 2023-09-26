package touch.baton.tobe.domain.member.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.tobe.domain.member.command.SupporterRunnerPost;

import java.util.Optional;

public interface SupporterRunnerPostCommandRepository extends JpaRepository<SupporterRunnerPost, Long> {

    @Query("""
            select count(1)
            from SupporterRunnerPost srp
            group by srp.runnerPost.id
            having srp.runnerPost.id = :runnerPostId
            """)
    Optional<Long> countByRunnerPostId(@Param("runnerPostId") final Long runnerPostId);

    boolean existsByRunnerPostId(final Long runnerPostId);

    boolean existsByRunnerPostIdAndSupporterId(final Long runnerPostId, final Long supporterId);

    void deleteBySupporterIdAndRunnerPostId(final Long supporterId, final Long runnerPostId);
}
