package touch.baton.domain.runnerpost.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.supporter.Supporter;

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

    List<RunnerPost> findAllByOrderByCreatedAtDesc();

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

    List<RunnerPost> findBySupporterAndReviewStatusOrderByCreatedAtDesc(final Supporter supporter, final ReviewStatus reviewStatus);
}
