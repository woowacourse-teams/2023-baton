package touch.baton.tobe.domain.technicaltag.command;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import touch.baton.tobe.domain.member.command.Runner;
import touch.baton.tobe.domain.tag.exception.SupporterTechnicalTagDomainException;

import java.util.Objects;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class RunnerTechnicalTag {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "runner_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_runner_technical_tag_to_runner"))
    private Runner runner;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "technical_tag_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_runner_technical_tag_to_technical_tag"))
    private TechnicalTag technicalTag;

    @Builder
    private RunnerTechnicalTag(final Runner runner, final TechnicalTag technicalTag) {
        this(null, runner, technicalTag);
    }

    private RunnerTechnicalTag(final Long id, final Runner runner, final TechnicalTag technicalTag) {
        validateNotNull(runner, technicalTag);
        this.id = id;
        this.runner = runner;
        this.technicalTag = technicalTag;
    }

    private void validateNotNull(final Runner runner, final TechnicalTag technicalTag) {
        if (Objects.isNull(runner)) {
            throw new SupporterTechnicalTagDomainException("RunnerTechnicalTag 의 runner 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(technicalTag)) {
            throw new SupporterTechnicalTagDomainException("RunnerTechnicalTag 의 technicalTag 는 null 일 수 없습니다.");
        }
    }
}
