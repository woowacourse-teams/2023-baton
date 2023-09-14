package touch.baton.assure.repository;

import touch.baton.domain.runnerpost.repository.RunnerPostReadRepository;

import java.util.List;

public interface TestRunnerPostReadRepository extends RunnerPostReadRepository {

    default Long countApplicantByRunnerPostId(final Long runnerPostId) {
        final List<Long> foundApplicants = countApplicantsByRunnerPostIds(List.of(runnerPostId));
        if (foundApplicants.isEmpty()) {
            throw new IllegalArgumentException("테스트에서 러너 게시글 식별자값으로 서포터 지원자 수 조회에 실패하였습니다.");
        }

        return foundApplicants.get(0);
    }
}
