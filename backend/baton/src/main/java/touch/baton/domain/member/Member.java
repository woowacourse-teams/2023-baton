package touch.baton.domain.member;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import touch.baton.domain.member.exception.MemberException;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.Email;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.Name;
import touch.baton.domain.member.vo.OauthId;

import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Member {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Email email;

    @Embedded
    private OauthId oauthId;

    @Embedded
    private GithubUrl githubUrl;

    @Embedded
    private Company company;

    @Builder
    private Member(final Name name,
                  final Email email,
                  final OauthId oauthId,
                  final GithubUrl githubUrl,
                  final Company company
    ) {
        this(null, name, email, oauthId, githubUrl, company);
    }

    private Member(final Long id,
                   final Name name,
                   final Email email,
                   final OauthId oauthId,
                   final GithubUrl githubUrl,
                   final Company company
    ) {
        validateNotNull(name, email, oauthId, githubUrl, company);
        this.id = id;
        this.name = name;
        this.email = email;
        this.oauthId = oauthId;
        this.githubUrl = githubUrl;
        this.company = company;
    }

    private void validateNotNull(final Name name,
                                 final Email email,
                                 final OauthId oauthId,
                                 final GithubUrl githubUrl,
                                 final Company company
    ) {
        if (Objects.isNull(name)) {
            throw new MemberException.NotNull("name 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(email)) {
            throw new MemberException.NotNull("name 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(oauthId)) {
            throw new MemberException.NotNull("name 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(githubUrl)) {
            throw new MemberException.NotNull("name 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(company)) {
            throw new MemberException.NotNull("name 는 null 일 수 없습니다.");
        }
    }
}
