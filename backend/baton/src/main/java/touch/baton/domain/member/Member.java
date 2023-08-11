package touch.baton.domain.member;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import touch.baton.domain.common.BaseEntity;
import touch.baton.domain.member.exception.MemberDomainException;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.SocialId;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.ImageUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;

import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Member extends BaseEntity {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Embedded
    private MemberName memberName;

    @Embedded
    private SocialId socialId;

    @Embedded
    private OauthId oauthId;

    @Embedded
    private GithubUrl githubUrl;

    @Embedded
    private Company company;

    @Embedded
    private ImageUrl imageUrl;

    @Builder
    private Member(final MemberName memberName,
                   final SocialId socialId,
                   final OauthId oauthId,
                   final GithubUrl githubUrl,
                   final Company company,
                   final ImageUrl imageUrl
    ) {
        this(null, memberName, socialId, oauthId, githubUrl, company, imageUrl);
    }

    private Member(final Long id,
                   final MemberName memberName,
                   final SocialId socialId,
                   final OauthId oauthId,
                   final GithubUrl githubUrl,
                   final Company company,
                   final ImageUrl imageUrl
    ) {
        validateNotNull(memberName, socialId, oauthId, githubUrl, company, imageUrl);
        this.id = id;
        this.memberName = memberName;
        this.socialId = socialId;
        this.oauthId = oauthId;
        this.githubUrl = githubUrl;
        this.company = company;
        this.imageUrl = imageUrl;
    }

    private void validateNotNull(final MemberName memberName,
                                 final SocialId socialId,
                                 final OauthId oauthId,
                                 final GithubUrl githubUrl,
                                 final Company company,
                                 final ImageUrl imageUrl
    ) {
        if (Objects.isNull(memberName)) {
            throw new MemberDomainException("Member 의 name 은 null 일 수 없습니다.");
        }

        if (Objects.isNull(socialId)) {
            throw new MemberDomainException("Member 의 socialId 은 null 일 수 없습니다.");
        }

        if (Objects.isNull(oauthId)) {
            throw new MemberDomainException("Member 의 oauthId 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(githubUrl)) {
            throw new MemberDomainException("Member 의 githubUrl 은 null 일 수 없습니다.");
        }

        if (Objects.isNull(company)) {
            throw new MemberDomainException("Member 의 company 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(imageUrl)) {
            throw new MemberDomainException("Member 의 imageUrl 은 null 일 수 없습니다.");
        }
    }
}
