package touch.baton.assure.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.supporter.SupporterRunnerPost;
import touch.baton.domain.supporter.repository.SupporterRunnerPostRepository;

import java.util.List;

public interface TestSupporterRunnerPostRepository extends SupporterRunnerPostRepository {

    default Long getApplicantCountByRunnerPostId(final Long runnerPostId) {
        return countByRunnerPostId(runnerPostId).orElse(0L);
    }

    @Query("""
            select srp, s, m
            from SupporterRunnerPost srp
            join fetch Supporter s on srp.supporter.id = s.id
            join fetch SupporterTechnicalTag stg on s.id = stg.supporter.id
            join fetch TechnicalTag tg on stg.technicalTag.id = tg.id
            join fetch Member m on s.member.id = m.id
            where srp.runnerPost.id = :runnerPostId
            """)
    List<SupporterRunnerPost> joinByRunnerPostId(@Param("runnerPostId") final Long runnerPostId);
}
