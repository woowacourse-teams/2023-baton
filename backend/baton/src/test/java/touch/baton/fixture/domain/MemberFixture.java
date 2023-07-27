package touch.baton.fixture.domain;

import touch.baton.domain.member.Member;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.Email;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.ImageUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;

import static touch.baton.fixture.vo.CompanyFixture.company;
import static touch.baton.fixture.vo.EmailFixture.email;
import static touch.baton.fixture.vo.GithubUrlFixture.githubUrl;
import static touch.baton.fixture.vo.ImageUrlFixture.imageUrl;
import static touch.baton.fixture.vo.MemberNameFixture.memberName;
import static touch.baton.fixture.vo.OauthIdFixture.oauthId;

public abstract class MemberFixture {

    private MemberFixture() {
    }

    public static Member create(final MemberName memberName,
                                final Email email,
                                final OauthId oauthId,
                                final GithubUrl githubUrl,
                                final Company company,
                                final ImageUrl imageUrl
    ) {
        return Member.builder()
                .memberName(memberName)
                .email(email)
                .oauthId(oauthId)
                .githubUrl(githubUrl)
                .company(company)
                .imageUrl(imageUrl)
                .build();
    }

    public static Member createHyena() {
        return create(
                memberName("헤나"),
                email("email_hyena@test.com"),
                oauthId("oauth_hyena"),
                githubUrl("https://github.com/"),
                company("우아한테크코스 5기 백엔드"),
                imageUrl("https://"));
    }

    public static Member createEthan() {
        return create(
                memberName("에단"),
                email("email_ethan@test.com"),
                oauthId("oauth_ethan"),
                githubUrl("https://github.com/"),
                company("우아한테크코스 5기 백엔드"),
                imageUrl("https://"));
    }

    public static Member createDitoo() {
        return create(
                memberName("디투"),
                email("email_ditoo@test.com"),
                oauthId("oauth_ditoo"),
                githubUrl("https://github.com/"),
                company("우아한테크코스 5기 백엔드"),
                imageUrl("https://"));
    }

    public static Member createJudy() {
        return create(
                memberName("주디"),
                email("email_judy@test.com"),
                oauthId("oauth_judy"),
                githubUrl("https://github.com/"),
                company("우아한테크코스 5기 백엔드"),
                imageUrl("https://"));
    }
}
