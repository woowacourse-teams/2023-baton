package touch.baton.fixture.domain;

import touch.baton.domain.member.Member;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.SocialId;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.ImageUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;

import static touch.baton.fixture.vo.CompanyFixture.company;
import static touch.baton.fixture.vo.SocialIdFixture.socialId;
import static touch.baton.fixture.vo.GithubUrlFixture.githubUrl;
import static touch.baton.fixture.vo.ImageUrlFixture.imageUrl;
import static touch.baton.fixture.vo.MemberNameFixture.memberName;
import static touch.baton.fixture.vo.OauthIdFixture.oauthId;

public abstract class MemberFixture {

    private MemberFixture() {
    }

    public static Member create(final MemberName memberName,
                                final SocialId socialId,
                                final OauthId oauthId,
                                final GithubUrl githubUrl,
                                final Company company,
                                final ImageUrl imageUrl
    ) {
        return Member.builder()
                .memberName(memberName)
                .socialId(socialId)
                .oauthId(oauthId)
                .githubUrl(githubUrl)
                .company(company)
                .imageUrl(imageUrl)
                .build();
    }

    public static Member createHyena() {
        return create(
                memberName("헤나"),
                socialId("hyenaSocialId"),
                oauthId("oauth_hyena"),
                githubUrl("https://github.com/"),
                company("우아한테크코스 5기 백엔드"),
                imageUrl("https://"));
    }

    public static Member createEthan() {
        return create(
                memberName("에단"),
                socialId("ethanSocialId"),
                oauthId("oauth_ethan"),
                githubUrl("https://github.com/"),
                company("우아한테크코스 5기 백엔드"),
                imageUrl("https://"));
    }

    public static Member createDitoo() {
        return create(
                memberName("디투"),
                socialId("ditooSocialId"),
                oauthId("oauth_ditoo"),
                githubUrl("https://github.com/"),
                company("우아한테크코스 5기 백엔드"),
                imageUrl("https://"));
    }

    public static Member createJudy() {
        return create(
                memberName("주디"),
                socialId("judySocialId"),
                oauthId("oauth_judy"),
                githubUrl("https://github.com/"),
                company("우아한테크코스 5기 백엔드"),
                imageUrl("https://"));
    }
}
