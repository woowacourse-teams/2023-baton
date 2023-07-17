package touch.baton.member.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import touch.baton.member.domain.vo.Company;
import touch.baton.member.domain.vo.Email;
import touch.baton.member.domain.vo.GithubUrl;
import touch.baton.member.domain.vo.MemberId;
import touch.baton.member.domain.vo.Name;
import touch.baton.member.domain.vo.OauthId;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Member {

    @EmbeddedId
    private MemberId id;

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
    public Member(final Name name,
                  final Email email,
                  final OauthId oauthId,
                  final GithubUrl githubUrl,
                  final Company company
    ) {
        this(null, name, email, oauthId, githubUrl, company);
    }

    public Member(final MemberId id,
                  final Name name,
                  final Email email,
                  final OauthId oauthId,
                  final GithubUrl githubUrl,
                  final Company company
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.oauthId = oauthId;
        this.githubUrl = githubUrl;
        this.company = company;
    }
}
