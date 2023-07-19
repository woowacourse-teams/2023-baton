package touch.baton.assure.fixture;

import touch.baton.domain.member.Member;
import touch.baton.domain.member.vo.*;

public abstract class MemberFixture {

    private MemberFixture() {
    }

    public static Member from(final String memberName,
                              final String email,
                              final String oauthId,
                              final String githubUrl,
                              final String company,
                              final String imageUrl
    ) {
        return Member.builder()
                .memberName(new MemberName(memberName))
                .email(new Email(email))
                .oauthId(new OauthId(oauthId))
                .githubUrl(new GithubUrl(githubUrl))
                .company(new Company(company))
                .imageUrl(new ImageUrl(imageUrl))
                .build();
    }
}
