package touch.baton.domain.member;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import touch.baton.domain.common.BaseEntity;
import touch.baton.domain.member.exception.MemberException;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.Email;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.ImageUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;

import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Member extends BaseEntity {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Embedded
    private MemberName memberName;

    @Embedded
    private Email email;

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
                  final Email email,
                  final OauthId oauthId,
                  final GithubUrl githubUrl,
                  final Company company,
                   final ImageUrl imageUrl
    ) {
        this(null, memberName, email, oauthId, githubUrl, company, imageUrl);
    }

    private Member(final Long id,
                   final MemberName memberName,
                   final Email email,
                   final OauthId oauthId,
                   final GithubUrl githubUrl,
                   final Company company,
                   final ImageUrl imageUrl
    ) {
        validateNotNull(memberName, email, oauthId, githubUrl, company, imageUrl);
        this.id = id;
        this.memberName = memberName;
        this.email = email;
        this.oauthId = oauthId;
        this.githubUrl = githubUrl;
        this.company = company;
        this.imageUrl = imageUrl;
    }

    private void validateNotNull(final MemberName memberName,
                                 final Email email,
                                 final OauthId oauthId,
                                 final GithubUrl githubUrl,
                                 final Company company,
                                 final ImageUrl imageUrl
    ) {
        if (Objects.isNull(memberName)) {
            throw new MemberException.NotNull("name 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(email)) {
            throw new MemberException.NotNull("email 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(oauthId)) {
            throw new MemberException.NotNull("oauthId 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(githubUrl)) {
            throw new MemberException.NotNull("githubUrl 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(company)) {
            throw new MemberException.NotNull("company 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(imageUrl)) {
            throw new MemberException.NotNull("imageUrl 는 null 일 수 없습니다.");
        }
    }
}
