package touch.baton.domain.member.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.member.command.SupporterRunnerPost;

import java.util.List;
import java.util.Optional;

public interface SupporterRunnerPostQueryRepository extends JpaRepository<SupporterRunnerPost, Long> {

    @Query("""
            select count(1)
            from SupporterRunnerPost srp
            group by srp.runnerPost.id
            having srp.runnerPost.id = :runnerPostId
            """)
    Optional<Long> countByRunnerPostId(@Param("runnerPostId") final Long runnerPostId);

    @Query("""
            select (count(1) >= 1)
            from SupporterRunnerPost srp
            join fetch Member m on m.id = srp.supporter.member.id
            where srp.runnerPost.id = :runnerPostId
            and srp.supporter.member.id = :memberId
            """)
    boolean existsByRunnerPostIdAndMemberId(@Param("runnerPostId") final Long runnerPostId, @Param("memberId") final Long memberId);

    List<SupporterRunnerPost> readByRunnerPostId(final Long runnerPostId);

    @Query("""
            select count(1)
            from SupporterRunnerPost srp
            join fetch RunnerPost rp on rp.id = srp.runnerPost.id
            where rp.reviewStatus = 'NOT_STARTED' and srp.supporter.id = :supporterId
            """)
    long countRunnerPostBySupporterIdByReviewStatusNotStarted(@Param("supporterId") final Long supporterId);
}
