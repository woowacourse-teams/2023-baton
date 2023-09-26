package touch.baton.assure.repository;

import org.springframework.context.annotation.Profile;
import touch.baton.tobe.domain.member.query.repository.SupporterRunnerPostQueryRepository;

@Profile("test")
public interface TestSupporterRunnerPostQueryRepository extends SupporterRunnerPostQueryRepository {

    default Long getApplicantCountByRunnerPostId(final Long runnerPostId) {
        return countByRunnerPostId(runnerPostId).orElse(0L);
    }
}
