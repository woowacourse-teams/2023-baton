package touch.baton.domain.runnerpost.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.repository.dto.ApplicantCountDto;
import touch.baton.domain.runnerpost.repository.dto.ApplicantCountMappingDto;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.tag.vo.TagReducedName;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface RunnerPostReadRepository extends JpaRepository<RunnerPost, Long> {

    default ApplicantCountMappingDto findApplicantCountMappingByRunnerPostIds(final List<Long> runnerPostIds) {
        final Map<Long, Long> applicantCountMapping = countApplicantsByRunnerPostIds(runnerPostIds)
                .stream()
                .collect(Collectors.toMap(
                        ApplicantCountDto::runnerPostId,
                        ApplicantCountDto::applicantCount
                ));

        return new ApplicantCountMappingDto(applicantCountMapping);
    }

    @Query(value = """
            select new touch.baton.domain.runnerpost.repository.dto.ApplicantCountDto(rp.id, count(srp.id)) 
            from RunnerPost rp
            left outer join fetch SupporterRunnerPost srp on srp.runnerPost.id = rp.id
            where rp.id in :runnerPostIds
            group by rp.id
            """)
    List<ApplicantCountDto> countApplicantsByRunnerPostIds(@Param("runnerPostIds") final List<Long> runnerPostIds);

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
