package touch.baton.assure.repository;

import org.springframework.data.repository.query.Param;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;

public interface TestRunnerPostRepository extends RunnerPostRepository {

    default RunnerPost getByRunnerPostId(@Param("runnerPostId") final Long runnerPostId) {
        return joinMemberByRunnerPostId(runnerPostId)
                .orElseThrow(() -> new IllegalArgumentException("테스트에서 RunnerPost 를 러너 게시글 식별자값(id) 로 조회할 수 없습니다."));
    }
}
