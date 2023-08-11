package touch.baton.domain.runner;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import touch.baton.domain.common.BaseEntity;
import touch.baton.domain.common.vo.Introduction;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.exception.RunnerDomainException;
import touch.baton.domain.technicaltag.RunnerTechnicalTag;
import touch.baton.domain.technicaltag.RunnerTechnicalTags;

import java.util.List;
import java.util.Objects;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Runner extends BaseEntity {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Embedded
    private Introduction introduction;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_runner_to_member"), nullable = false)
    private Member member;

    @Embedded
    private RunnerTechnicalTags runnerTechnicalTags;

    @Builder
    private Runner(final Introduction introduction,
                   final Member member,
                   final RunnerTechnicalTags runnerTechnicalTags
    ) {
        this(null, introduction, member, runnerTechnicalTags);
    }

    private Runner(final Long id,
                   final Introduction introduction,
                   final Member member,
                   final RunnerTechnicalTags runnerTechnicalTags
    ) {
        validateMemberNotNull(member);
        this.id = id;
        this.introduction = introduction;
        this.member = member;
        this.runnerTechnicalTags = runnerTechnicalTags;
    }

    private void validateMemberNotNull(final Member member) {
        if (Objects.isNull(member)) {
            throw new RunnerDomainException("Runner 의 member 는 null 일 수 없습니다.");
        }
    }

    public void addAllRunnerTechnicalTags(final List<RunnerTechnicalTag> runnerTechnicalTags) {
        this.runnerTechnicalTags.addAll(runnerTechnicalTags);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Runner runner = (Runner) o;
        return Objects.equals(id, runner.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
