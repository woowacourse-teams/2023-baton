package touch.baton.domain.runner;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.vo.Grade;
import touch.baton.domain.runner.vo.RunnerId;
import touch.baton.domain.runner.vo.TotalRating;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Runner {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Embedded
    private TotalRating totalRating;

    @Enumerated(STRING)
    private Grade grade;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_runner_member"))
    private Member member;

    public Runner(final TotalRating totalRating, final Grade grade, final Member member) {
        this(null, totalRating, grade, member);
    }

    public Runner(final RunnerId runnerId, final TotalRating totalRating, final Grade grade, final Member member) {
        this.runnerId = runnerId;
        this.totalRating = totalRating;
        this.grade = grade;
        this.member = member;
    }
}
