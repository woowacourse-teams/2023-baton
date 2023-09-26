package touch.baton.domain.oauth;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import touch.baton.tobe.domain.member.command.vo.GithubUrl;
import touch.baton.tobe.domain.member.command.vo.ImageUrl;
import touch.baton.tobe.domain.member.command.vo.MemberName;
import touch.baton.tobe.domain.member.command.vo.OauthId;
import touch.baton.tobe.domain.member.command.vo.SocialId;
import touch.baton.domain.oauth.token.SocialToken;

import static lombok.AccessLevel.PROTECTED;

@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = PROTECTED)
public class OauthInformation {

    private SocialToken socialToken;

    private OauthId oauthId;

    private MemberName memberName;

    private SocialId socialId;

    private GithubUrl githubUrl;

    private ImageUrl imageUrl;

    @Builder
    private OauthInformation(final SocialToken socialToken,
                             final OauthId oauthId,
                             final MemberName memberName,
                             final SocialId socialId,
                             final GithubUrl githubUrl,
                             final ImageUrl imageUrl
    ) {
        this.socialToken = socialToken;
        this.oauthId = oauthId;
        this.memberName = memberName;
        this.socialId = socialId;
        this.githubUrl = githubUrl;
        this.imageUrl = imageUrl;
    }
}
