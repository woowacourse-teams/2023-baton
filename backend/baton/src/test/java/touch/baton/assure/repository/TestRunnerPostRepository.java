package touch.baton.assure.repository;

import org.springframework.data.repository.query.Param;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.repository.RunnerPostCustomRepository;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;
import touch.baton.domain.runnerpost.repository.dto.RunnerPostApplicantCountDto;

import java.util.List;

public interface TestRunnerPostRepository extends RunnerPostRepository, RunnerPostCustomRepository {

    default RunnerPost getByRunnerPostId(@Param("runnerPostId") final Long runnerPostId) {
        return joinMemberByRunnerPostId(runnerPostId)
                .orElseThrow(() -> new IllegalArgumentException("테스트에서 RunnerPost 를 러너 게시글 식별자값(id) 로 조회할 수 없습니다."));
    }

    default long countApplicantByRunnerPostId(final Long runnerPostId) {
        final List<RunnerPostApplicantCountDto> foundApplicants = countApplicantsByRunnerPostIds(List.of(runnerPostId));
        if (foundApplicants.isEmpty()) {
            throw new IllegalArgumentException("테스트에서 러너 게시글 식별자값으로 서포터 지원자 수 조회에 실패하였습니다.");
        }

        return foundApplicants.get(0).applicantCount();
    }
}
