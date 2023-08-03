package touch.baton.infra.auth.oauth.github.response;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import touch.baton.domain.member.vo.Email;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.ImageUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;
import touch.baton.domain.oauth.SocialToken;
import touch.baton.domain.oauth.OauthInformation;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

@JsonNaming(SnakeCaseStrategy.class)
public record GithubMemberResponse(String id,
                                   String name,
                                   String email,
                                   String htmlUrl,
                                   String avatarUrl
) {

    public OauthInformation toOauthInformation(final String accessToken) {
        return OauthInformation.builder()
                .socialToken(new SocialToken(accessToken))
                .oauthId(new OauthId(id))
                .memberName(new MemberName(name))
                .email(new Email(email))
                .githubUrl(new GithubUrl(htmlUrl))
                .imageUrl(new ImageUrl(avatarUrl))
                .build();
    }
}
