package touch.baton.domain.runnerpost.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.repository.dto.RunnerPostApplicantCountDto;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.tag.vo.TagReducedName;

import java.util.List;
import java.util.Optional;

public interface RunnerPostRepository extends JpaRepository<RunnerPost, Long>, RunnerPostCustomRepository {

    @Query(value = """
            select rp, r, m
            from RunnerPost rp
            join fetch Runner r on r.id = rp.runner.id
            join fetch Member m on m.id = r.member.id
            where rp.id = :runnerPostId
            """)
    Optional<RunnerPost> joinMemberByRunnerPostId(@Param("runnerPostId") final Long runnerPostId);

    Page<RunnerPost> findByReviewStatus(final Pageable pageable, final ReviewStatus reviewStatus);

    @Query(countQuery = """
            select count(1)
            from RunnerPost rp
            where rp.runner.id = :runnerId
            and rp.reviewStatus = :reviewStatus
            """)
    Page<RunnerPost> findByRunnerIdAndReviewStatus(final Pageable pageable,
                                                   @Param("runnerId") final Long runnerId,
                                                   @Param("reviewStatus") final ReviewStatus reviewStatus);

    List<RunnerPost> findByRunnerId(final Long runnerId);

    @Query(countQuery = """
            select count(1)
            from RunnerPost rp
            where rp.supporter.id = :supporterId
            and rp.reviewStatus = :reviewStatus
            """)
    Page<RunnerPost> findBySupporterIdAndReviewStatus(final Pageable pageable,
                                                      @Param("supporterId") final Long supporterId,
                                                      @Param("reviewStatus") final ReviewStatus reviewStatus);

    @Query("""
            select rp
            from RunnerPost rp
            join fetch SupporterRunnerPost srp on srp.runnerPost.id = rp.id
            where srp.supporter.id = :supporterId
            and rp.reviewStatus = :reviewStatus
            """)
    Page<RunnerPost> joinSupporterRunnerPostBySupporterIdAndReviewStatus(
            final Pageable pageable,
            @Param("supporterId") final Long supporterId,
            @Param("reviewStatus") final ReviewStatus reviewStatus);

    @Query("""
            select new touch.baton.domain.runnerpost.repository.dto.RunnerPostApplicantCountDto(rp.id, coalesce(count(srp.id), 0L))
            from RunnerPost rp
            left join SupporterRunnerPost srp on srp.runnerPost.id = rp.id
            where rp.id in :runnerPostIds
            group by rp.id
            """)
    List<RunnerPostApplicantCountDto> countApplicantsByRunnerPostIds(@Param("runnerPostIds") final List<Long> runnerPostIds);

    @Query("""
            select rp
            from RunnerPost rp
            join fetch Runner r on r.id = rp.runner.id
            join fetch Member m on m.id = r.member.id
            join fetch RunnerPostTag rpt on rpt.runnerPost.id = rp.id
            
            where rpt.tag.tagReducedName = :tagReducedName
            and rp.reviewStatus = :reviewStatus
            """)
    Page<RunnerPost> findByTagReducedNameAndReviewStatus(
            final Pageable pageable,
            @Param("tagReducedName") final TagReducedName tagReducedName,
            @Param("reviewStatus") final ReviewStatus reviewStatus);
}
