package touch.baton.domain.runnerpost;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.domain.runnerpost.exception.RunnerPostBusinessException;
import touch.baton.domain.runnerpost.repository.dto.RunnerPostApplicantCountDto;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class RunnerPostsApplicantCountTest {

    @DisplayName("게시글 지원자 수 DTO 목록으로 생성한다.")
    @Test
    void createByRunnerPostApplicantCountDto() {
        // given
        final List<RunnerPostApplicantCountDto> dtos = new ArrayList<>();
        final long applicantCount = 3;
        for (long runnerPostId = 1; runnerPostId <= 10; runnerPostId++) {
            dtos.add(new RunnerPostApplicantCountDto(runnerPostId, applicantCount));
        }

        // when, then
        assertThatCode(() -> RunnerPostsApplicantCount.from(dtos))
                .doesNotThrowAnyException();
    }

    @DisplayName("게시글 지원자 수 DTO 목록이 null 이면 예외가 발생한다.")
    @Test
    void createByRunnerPostApplicantCountDto_fail_when_dto_is_null() {
        // given
        final List<RunnerPostApplicantCountDto> dtos = null;

        // when, then
        assertThatThrownBy(() -> RunnerPostsApplicantCount.from(dtos))
                .isInstanceOf(RunnerPostBusinessException.class);
    }

    @DisplayName("러너 게시글 지원자 수를 조회한다.")
    @Test
    void readRunnerPostsApplicantCount() {
        // given
        final List<RunnerPostApplicantCountDto> dtos = new ArrayList<>();
        final Long runnerPostId = 1L;
        final Long applicantCount = 3L;
        dtos.add(new RunnerPostApplicantCountDto(runnerPostId, applicantCount));
        final RunnerPostsApplicantCount runnerPostsApplicantCount = RunnerPostsApplicantCount.from(dtos);

        // when
        final Long actual = runnerPostsApplicantCount.getApplicantCountById(runnerPostId);

        // then
        assertThat(actual).isEqualTo(applicantCount);
    }

    @DisplayName("러너 게시글 지원자 수를 조회할 때 없는 runnerPostId 로 조회하면 예외가 발생한다.")
    @Test
    void readRunnerPostsApplicantCount_fail_if_id_not_exist() {
        // given
        final List<RunnerPostApplicantCountDto> dtos = new ArrayList<>();
        final Long runnerPostId = 1L;
        final Long applicantCount = 3L;
        dtos.add(new RunnerPostApplicantCountDto(runnerPostId, applicantCount));
        final RunnerPostsApplicantCount runnerPostsApplicantCount = RunnerPostsApplicantCount.from(dtos);

        // when, then
        final Long invalidRunnerPostId = 0L;
        assertThatThrownBy(() -> runnerPostsApplicantCount.getApplicantCountById(invalidRunnerPostId))
                .isInstanceOf(RunnerPostBusinessException.class);
    }
}
