package touch.baton.infra.oauth.github.response;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.Email;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.ImageUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;
import touch.baton.domain.oauth.OauthInformation;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

@JsonNaming(SnakeCaseStrategy.class)
public record GithubMemberResponse(String id,
                                   String name,
                                   String email,
                                   String htmlUrl,
                                   String avatarUrl,
                                   String company
) {

    public OauthInformation toOauthInformation() {
        return OauthInformation.builder()
                .oauthId(new OauthId(id))
                .memberName(new MemberName(name))
                .email(new Email(email))
                .githubUrl(new GithubUrl(htmlUrl))
                .imageUrl(new ImageUrl(avatarUrl))
                .company(new Company(company))
                .build();
    }
}
