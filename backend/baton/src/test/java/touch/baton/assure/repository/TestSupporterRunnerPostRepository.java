package touch.baton.assure.repository;

import touch.baton.domain.supporter.repository.SupporterRunnerPostRepository;

public interface TestSupporterRunnerPostRepository extends SupporterRunnerPostRepository {

    default Long getApplicantCountByRunnerPostId(final Long runnerPostId) {
        return countByRunnerPostId(runnerPostId).orElse(0L);
    }
}
