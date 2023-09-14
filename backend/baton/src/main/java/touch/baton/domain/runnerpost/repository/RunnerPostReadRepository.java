package touch.baton.domain.runnerpost.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.tag.vo.TagReducedName;

import java.util.List;

public interface RunnerPostReadRepository extends JpaRepository<RunnerPost, Long> {

    @Query("""
            select coalesce(count(srp.id), 0) 
            from RunnerPost rp
            left outer join fetch SupporterRunnerPost srp on srp.runnerPost.id = rp.id
            where rp.id in :runnerPostIds
            group by rp.id
            """)
    List<Long> countApplicantsByRunnerPostIds(@Param("runnerPostIds") final List<Long> runnerPostIds);

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
