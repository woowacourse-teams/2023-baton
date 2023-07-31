package touch.baton.domain.oauth;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.Email;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.ImageUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;

import static lombok.AccessLevel.PROTECTED;

@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = PROTECTED)
public class OauthInformation {

    private OauthId oauthId;

    private MemberName memberName;

    private Email email;

    private GithubUrl githubUrl;

    private ImageUrl imageUrl;

    private Company company;

    @Builder
    private OauthInformation(final OauthId oauthId,
                             final MemberName memberName,
                             final Email email,
                             final GithubUrl githubUrl,
                             final ImageUrl imageUrl,
                             final Company company
    ) {
        this.oauthId = oauthId;
        this.memberName = memberName;
        this.email = email;
        this.githubUrl = githubUrl;
        this.imageUrl = imageUrl;
        this.company = company;
    }
}
