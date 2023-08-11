package touch.baton.infra.auth.oauth.github.response;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.ImageUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;
import touch.baton.domain.member.vo.SocialId;
import touch.baton.domain.oauth.OauthInformation;
import touch.baton.domain.oauth.SocialToken;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

@JsonNaming(SnakeCaseStrategy.class)
public record GithubMemberResponse(String id,
                                   String name,
                                   String login,
                                   String htmlUrl,
                                   String avatarUrl
) {

    public OauthInformation toOauthInformation(final String accessToken) {
        return OauthInformation.builder()
                .socialToken(new SocialToken(accessToken))
                .oauthId(new OauthId(id))
                .memberName(new MemberName(name))
                .socialId(new SocialId(login))
                .githubUrl(new GithubUrl(htmlUrl))
                .imageUrl(new ImageUrl(avatarUrl))
                .build();
    }
}
