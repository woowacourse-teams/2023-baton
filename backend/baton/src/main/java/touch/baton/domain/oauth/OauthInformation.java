package touch.baton.domain.oauth;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.ImageUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;
import touch.baton.domain.member.vo.SocialId;

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
