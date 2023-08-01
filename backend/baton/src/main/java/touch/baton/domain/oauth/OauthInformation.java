package touch.baton.domain.oauth;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    private AccessToken accessToken;

    private OauthId oauthId;

    private MemberName memberName;

    private Email email;

    private GithubUrl githubUrl;

    private ImageUrl imageUrl;

    @Builder
    private OauthInformation(final AccessToken accessToken,
                             final OauthId oauthId,
                             final MemberName memberName,
                             final Email email,
                             final GithubUrl githubUrl,
                             final ImageUrl imageUrl
    ) {
        this.accessToken = accessToken;
        this.oauthId = oauthId;
        this.memberName = memberName;
        this.email = email;
        this.githubUrl = githubUrl;
        this.imageUrl = imageUrl;
    }
}
