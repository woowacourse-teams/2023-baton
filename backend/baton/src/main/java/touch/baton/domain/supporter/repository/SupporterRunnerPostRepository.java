package touch.baton.domain.supporter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.supporter.SupporterRunnerPost;

import java.util.List;
import java.util.Optional;

public interface SupporterRunnerPostRepository extends JpaRepository<SupporterRunnerPost, Long> {

    @Query("""
            select count(1)
            from SupporterRunnerPost srp
            group by srp.runnerPost.id
            having srp.runnerPost.id in (:runnerPostIds)
            """)
    List<Long> countByRunnerPostIdIn(@Param("runnerPostIds") final List<Long> runnerPostIds);

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

    boolean existsByRunnerPostId(final Long runnerPostId);

    void deleteBySupporterIdAndRunnerPostId(final Long supporterId, final Long runnerPostId);

    boolean existsByRunnerPostIdAndSupporterId(final Long runnerPostId, final Long supporterId);

    List<SupporterRunnerPost> readByRunnerPostId(final Long runnerPostId);
}
