package touch.baton.domain.runnerpost.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.repository.dto.RunnerPostApplicantCountDto;
import touch.baton.domain.runnerpost.command.vo.ReviewStatus;

import java.util.List;
import java.util.Optional;

public interface RunnerPostQueryRepository extends JpaRepository<RunnerPost, Long> {

    @Query(value = """
            select rp, r, m
            from RunnerPost rp
            join fetch Runner r on r.id = rp.runner.id
            join fetch Member m on m.id = r.member.id
            where rp.id = :runnerPostId
            """)
    Optional<RunnerPost> joinMemberByRunnerPostId(@Param("runnerPostId") final Long runnerPostId);

    @Query("""
        select rp
        from RunnerPost rp
        join fetch Supporter s on s.id = rp.supporter.id
        join fetch Member m on m.id = s.member.id
        where rp.id = :runnerPostId
        """)
    Optional<RunnerPost> joinSupporterByRunnerPostId(@Param("runnerPostId") final Long runnerPostId);

    List<RunnerPost> findByRunnerId(final Long runnerId);

    @Query("""
            select new touch.baton.domain.runnerpost.command.repository.dto.RunnerPostApplicantCountDto(rp.id, coalesce(count(srp.id), 0L))
            from RunnerPost rp
            left join SupporterRunnerPost srp on srp.runnerPost.id = rp.id
            where rp.id in :runnerPostIds
            group by rp.id
            """)
    List<RunnerPostApplicantCountDto> countApplicantsByRunnerPostIds(@Param("runnerPostIds") final List<Long> runnerPostIds);

    @Query("""
            select count(1)
            from RunnerPost rp
            where rp.runner.id = :runnerId and rp.reviewStatus = :reviewStatus
            """)
    long countByRunnerIdAndReviewStatus(@Param("runnerId") final Long runnerId, @Param("reviewStatus") final ReviewStatus reviewStatus);

    @Query("""
            select count(1)
            from RunnerPost rp
            where rp.supporter.id = :supporterId and rp.reviewStatus = :reviewStatus
            """)
    long countBySupporterIdAndReviewStatus(@Param("supporterId") final Long supporterId, @Param("reviewStatus") final ReviewStatus reviewStatus);


    @Query("""
            select count(1)
            from RunnerPost rp
            where rp.reviewStatus = :reviewStatus
            """)
    long countByReviewStatus(@Param("reviewStatus") ReviewStatus reviewStatus);
}
