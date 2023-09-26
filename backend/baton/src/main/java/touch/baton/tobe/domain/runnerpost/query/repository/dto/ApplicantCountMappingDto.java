package touch.baton.tobe.domain.runnerpost.query.repository.dto;

import java.util.Map;

public record ApplicantCountMappingDto(Map<Long, Long> applicantCounts) {

    public Long getApplicantCountByRunnerPostId(final Long runnerPostId) {
        return applicantCounts.get(runnerPostId);
    }
}
