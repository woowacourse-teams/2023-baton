package touch.baton.fixture.domain;

import touch.baton.domain.member.Member;

import static touch.baton.fixture.vo.CompanyFixture.company;
import static touch.baton.fixture.vo.EmailFixture.email;
import static touch.baton.fixture.vo.GithubUrlFixture.githubUrl;
import static touch.baton.fixture.vo.ImageUrlFixture.imageUrl;
import static touch.baton.fixture.vo.MemberNameFixture.memberName;
import static touch.baton.fixture.vo.OauthIdFixture.oauthId;

public abstract class MemberFixture {

    private MemberFixture() {
    }

    public static Member create(final String memberName,
                                final String email,
                                final String oauthId,
                                final String githubUrl,
                                final String company,
                                final String imageUrl
    ) {
        return Member.builder()
                .memberName(memberName(memberName))
                .email(email(email))
                .oauthId(oauthId(oauthId))
                .githubUrl(githubUrl(githubUrl))
                .company(company(company))
                .imageUrl(imageUrl(imageUrl))
                .build();
    }

    public static Member createHyena() {
        return create("헤나",
                "email_hyena@test.com",
                "oauth_hyena",
                "https://github.com/",
                "우아한테크코스 5기 백엔드",
                "https://");
    }

    public static Member createEthan() {
        return create(
                "에단",
                "email_ethan@test.com",
                "oauth_ethan",
                "https://github.com/",
                "우아한테크코스 5기 백엔드",
                "https://");
    }

    public static Member createDitoo() {
        return create(
                "디투",
                "email_ditoo@test.com",
                "oauth_ditoo",
                "https://github.com/",
                "우아한테크코스 5기 백엔드",
                "https://");
    }

    public static Member createJudy() {
        return create(
                "주디",
                "email_judy@test.com",
                "oauth_judy",
                "https://github.com/",
                "우아한테크코스 5기 백엔드",
                "https://");
    }
}
