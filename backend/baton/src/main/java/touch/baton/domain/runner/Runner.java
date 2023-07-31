package touch.baton.domain.runner;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import touch.baton.domain.common.BaseEntity;
import touch.baton.domain.common.vo.Grade;
import touch.baton.domain.common.vo.Introduction;
import touch.baton.domain.common.vo.TotalRating;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.exception.OldRunnerException;

import java.util.Objects;

import static jakarta.persistence.EnumType.STRING;
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
    private TotalRating totalRating;

    @Enumerated(STRING)
    @Column(nullable = false)
    private Grade grade;

    @Embedded
    private Introduction introduction;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_runner_to_member"), nullable = false)
    private Member member;

    @Builder
    private Runner(final TotalRating totalRating,
                   final Grade grade,
                   final Introduction introduction,
                   final Member member
    ) {
        this(null, totalRating, grade, introduction, member);
    }

    private Runner(final Long id,
                   final TotalRating totalRating,
                   final Grade grade,
                   final Introduction introduction,
                   final Member member
    ) {
        validateNotNull(totalRating, grade, member);
        this.id = id;
        this.totalRating = totalRating;
        this.grade = grade;
        this.introduction = introduction;
        this.member = member;
    }

    private void validateNotNull(final TotalRating totalRating, final Grade grade, final Member member) {
        if (Objects.isNull(totalRating)) {
            throw new OldRunnerException.NotNull("totalRating 은 null 일 수 없습니다.");
        }

        if (Objects.isNull(grade)) {
            throw new OldRunnerException.NotNull("grade 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(member)) {
            throw new OldRunnerException.NotNull("member 는 null 일 수 없습니다.");
        }
    }
}
