package touch.baton.domain.runnerpost.repository.dto;

import java.util.Map;
import java.util.Objects;

public class ApplicantCountMappingDto {

    private final Map<Long, Long> applicantCounts;

    public ApplicantCountMappingDto(final Map<Long, Long> applicantCounts) {
        this.applicantCounts = applicantCounts;
    }

    public Long getApplicantCountByRunnerPostId(final Long runnerPostId) {
        return applicantCounts.get(runnerPostId);
    }

    public Map<Long, Long> getApplicantCounts() {
        return applicantCounts;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ApplicantCountMappingDto that = (ApplicantCountMappingDto) o;
        return Objects.equals(applicantCounts, that.applicantCounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicantCounts);
    }
}
