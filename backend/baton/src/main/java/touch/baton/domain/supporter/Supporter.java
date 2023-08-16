package touch.baton.domain.supporter;

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
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.supporter.exception.SupporterDomainException;
import touch.baton.domain.supporter.vo.ReviewCount;
import touch.baton.domain.technicaltag.SupporterTechnicalTag;
import touch.baton.domain.technicaltag.SupporterTechnicalTags;

import java.util.List;
import java.util.Objects;

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
    private Introduction introduction = Introduction.getDefaultIntroduction();

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_supporter_to_member"), nullable = false)
    private Member member;

    @Embedded
    private SupporterTechnicalTags supporterTechnicalTags;

    @Builder
    private Supporter(final ReviewCount reviewCount,
                      final Introduction introduction,
                      final Member member,
                      final SupporterTechnicalTags supporterTechnicalTags
    ) {
        this(null, reviewCount, introduction, member, supporterTechnicalTags);
    }

    private Supporter(final Long id,
                      final ReviewCount reviewCount,
                      final Introduction introduction,
                      final Member member,
                      final SupporterTechnicalTags supporterTechnicalTags
    ) {
        validateNotNull(reviewCount, introduction, member, supporterTechnicalTags);
        this.id = id;
        this.introduction = introduction;
        this.reviewCount = reviewCount;
        this.member = member;
        this.supporterTechnicalTags = supporterTechnicalTags;
    }

    private void validateNotNull(final ReviewCount reviewCount,
                                 final Introduction introduction,
                                 final Member member,
                                 final SupporterTechnicalTags supporterTechnicalTags
    ) {
        validateReviewCountNotNull(reviewCount);
        validateMemberNotNull(member);
        validateSupporterTechnicalTagsNotNull(supporterTechnicalTags);
        updateIntroduction(introduction);
    }

    private void validateReviewCountNotNull(final ReviewCount reviewCount) {
        if (Objects.isNull(reviewCount)) {
            throw new SupporterDomainException("Supporter 의 reviewCount 는 null 일 수 없습니다.");
        }
    }

    private void validateMemberNotNull(final Member member) {
        if (Objects.isNull(member)) {
            throw new SupporterDomainException("Supporter 의 member 는 null 일 수 없습니다.");
        }
    }

    private void validateSupporterTechnicalTagsNotNull(final SupporterTechnicalTags supporterTechnicalTags) {
        if (Objects.isNull(supporterTechnicalTags)) {
            throw new SupporterDomainException("Supporter 의 supporterTechnicalTags 는 null 일 수 없습니다.");
        }
    }

    public void updateIntroduction(final Introduction introduction) {
        this.introduction = updateDefaultIntroductionIfNull(introduction);
    }

    private Introduction updateDefaultIntroductionIfNull(final Introduction introduction) {
        if (Objects.isNull(introduction) || Objects.isNull(introduction.getValue())) {
            return Introduction.getDefaultIntroduction();
        }
        return introduction;
    }

    public void addAllSupporterTechnicalTags(final List<SupporterTechnicalTag> supporterTechnicalTags) {
        this.supporterTechnicalTags.addAll(supporterTechnicalTags);
    }

    public void updateMemberName(final MemberName memberName) {
        this.member.updateMemberName(memberName);
    }

    public void updateCompany(final Company company) {
        this.member.updateCompany(company);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Supporter supporter = (Supporter) o;
        return Objects.equals(id, supporter.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
