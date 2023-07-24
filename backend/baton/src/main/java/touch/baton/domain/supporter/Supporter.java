package touch.baton.domain.supporter;

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
import touch.baton.domain.common.vo.TotalRating;
import touch.baton.domain.member.Member;
import touch.baton.domain.supporter.exception.OldSupporterException;
import touch.baton.domain.supporter.vo.ReviewCount;
import touch.baton.domain.supporter.vo.StarCount;

import java.util.Objects;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Supporter extends BaseEntity {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Embedded
    private ReviewCount reviewCount;

    @Embedded
    private StarCount starCount;

    @Embedded
    private TotalRating totalRating;

    @Enumerated(STRING)
    @Column(nullable = false)
    private Grade grade;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_supporter_member"), nullable = false)
    private Member member;

    @Builder
    private Supporter(final ReviewCount reviewCount,
                      final StarCount starCount,
                      final TotalRating totalRating,
                      final Grade grade,
                      final Member member
    ) {
        this(null, reviewCount, starCount, totalRating, grade, member);
    }

    private Supporter(final Long id,
                      final ReviewCount reviewCount,
                      final StarCount starCount,
                      final TotalRating totalRating,
                      final Grade grade,
                      final Member member
    ) {
        validateNotNull(reviewCount, starCount, totalRating, grade, member);
        this.id = id;
        this.reviewCount = reviewCount;
        this.starCount = starCount;
        this.totalRating = totalRating;
        this.grade = grade;
        this.member = member;
    }

    private void validateNotNull(final ReviewCount reviewCount,
                                 final StarCount starCount,
                                 final TotalRating totalRating,
                                 final Grade grade,
                                 final Member member
    ) {
        if (Objects.isNull(reviewCount)) {
            throw new OldSupporterException.NotNull("reviewCount 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(starCount)) {
            throw new OldSupporterException.NotNull("starCount 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(totalRating)) {
            throw new OldSupporterException.NotNull("totalRating 은 null 일 수 없습니다.");
        }

        if (Objects.isNull(grade)) {
            throw new OldSupporterException.NotNull("grade 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(member)) {
            throw new OldSupporterException.NotNull("member 는 null 일 수 없습니다.");
        }
    }
}
