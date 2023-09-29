package touch.baton.domain.runnerpost.query.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.repository.dto.RunnerPostApplicantCountDto;
import touch.baton.domain.runnerpost.command.vo.ReviewStatus;
import touch.baton.domain.tag.command.vo.TagReducedName;

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
