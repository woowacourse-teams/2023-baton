package touch.baton.domain.runnerpost;

import touch.baton.domain.runnerpost.exception.RunnerPostBusinessException;
import touch.baton.domain.runnerpost.repository.dto.RunnerPostApplicantCountDto;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class RunnerPostsApplicantCount {

    private final Map<Long, Long> runnerPostsApplicantCount;

    private RunnerPostsApplicantCount(final Map<Long, Long> runnerPostsApplicantCount) {
        this.runnerPostsApplicantCount = runnerPostsApplicantCount;
    }

    public static RunnerPostsApplicantCount from(final List<RunnerPostApplicantCountDto> dtos) {
        validateDtosNotNull(dtos);
        final Map<Long, Long> runnerPostApplicantCounts = dtos.stream()
                .collect(Collectors.toMap(
                        RunnerPostApplicantCountDto::runnerPostId,
                        RunnerPostApplicantCountDto::applicantCount
                ));
        return new RunnerPostsApplicantCount(runnerPostApplicantCounts);
    }

    private static void validateDtosNotNull(final List<RunnerPostApplicantCountDto> dtos) {
        if (Objects.isNull(dtos)) {
            throw new RunnerPostBusinessException("RunnerPostsApplicantCount 를 생성할 때 생성자인 dto 가 null 입니다.");
        }
    }

    public Long getApplicantCountById(final Long runnerPostId) {
        final Long readApplicantCount = runnerPostsApplicantCount.get(runnerPostId);
        validateElementExist(readApplicantCount);
        return readApplicantCount;
    }

    private void validateElementExist(final Long readElement) {
        if (Objects.isNull(readElement)) {
            throw new RunnerPostBusinessException(String.format("%s 에 없는 RunnerPostId 입니다.", this.getClass().getSimpleName()));
        }
    }
}
