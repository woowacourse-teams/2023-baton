package touch.baton.domain.runnerpost.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.vo.ReviewStatus;

import java.util.List;
import java.util.Optional;

public interface RunnerPostRepository extends JpaRepository<RunnerPost, Long> {

    @Query(value = """
            select rp
            from RunnerPost rp
            join fetch Runner r on r.id = rp.runner.id
            join fetch Member m on m.id = r.member.id
            where rp.id = :runnerPostId
            """)
    Optional<RunnerPost> joinMemberByRunnerPostId(@Param("runnerPostId") final Long runnerPostId);

    Page<RunnerPost> findAll(Pageable pageable);

    List<RunnerPost> findByRunnerId(Long runnerId);

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
    Page<RunnerPost> joinSupporterRunnerPostBySupporterIdAndReviewStatus(final Pageable pageable,
                                                                         @Param("supporterId") final Long supporterId,
                                                                         @Param("reviewStatus") final ReviewStatus reviewStatus);
}
