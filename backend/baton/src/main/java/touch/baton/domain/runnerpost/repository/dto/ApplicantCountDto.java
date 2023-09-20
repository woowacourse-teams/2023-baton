package touch.baton.domain.runnerpost.repository.dto;

import java.util.Objects;

public class ApplicantCountDto {

    private Long runnerPostId;

    private Long applicantCount;

    public ApplicantCountDto() {
    }

    public ApplicantCountDto(final Long runnerPostId, final Long applicantCount) {
        this.runnerPostId = runnerPostId;
        this.applicantCount = (long) applicantCount;
    }

    public Long getRunnerPostId() {
        return runnerPostId;
    }

    public Long getApplicantCount() {
        return applicantCount;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ApplicantCountDto that = (ApplicantCountDto) o;
        return applicantCount == that.applicantCount && Objects.equals(runnerPostId, that.runnerPostId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(runnerPostId, applicantCount);
    }

    @Override
    public String toString() {
        return "ApplicantCountDto{" +
               "runnerPostId=" + runnerPostId +
               ", applicantCount=" + applicantCount +
               '}';
    }
}
