package touch.baton.domain.runnerpost.repository.dto;

import java.util.Map;

public record ApplicantCountMappingDto(Map<Long, Long> applicantCounts) {

    public Long getApplicantCountByRunnerPostId(final Long runnerPostId) {
        return applicantCounts.get(runnerPostId);
    }
}
