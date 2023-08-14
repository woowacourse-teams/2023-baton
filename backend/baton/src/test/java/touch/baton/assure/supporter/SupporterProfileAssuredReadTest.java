package touch.baton.assure.supporter;

import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.controller.response.SupporterResponse;
import touch.baton.domain.technicaltag.TechnicalTag;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.TechnicalTagFixture;

import java.util.List;

import static touch.baton.assure.supporter.SupporterProfileAssuredSupport.서포터_프로필_응답;

@SuppressWarnings("NonAsciiCharacters")
class SupporterProfileAssuredReadTest extends AssuredTestConfig {

    @Test
    void 서포터_프로필을_조회한다() {
        final Member 사용자_헤나 = memberRepository.save(MemberFixture.createHyena());
        final Supporter 서포터_헤나 = supporterRepository.save(SupporterFixture.create(사용자_헤나));

        SupporterProfileAssuredSupport
                .클라이언트_요청()
                .서포터_프로필을_서포터_식별자값으로_조회한다(서포터_헤나.getId())

                .서버_응답()
                .서포터_프로필_조회_성공을_검증한다(서포터_프로필_응답(서포터_헤나));
    }

    @Test
    void 서포터_마이페이지_프로필을_조회한다() {
        // given
        final String 디투_소셜_아이디 = "hongsile";
        final Member 사용자_디투 = memberRepository.save(MemberFixture.createWithSocialId(디투_소셜_아이디));

        final TechnicalTag 자바_태그 = technicalTagRepository.save(TechnicalTagFixture.createJava());
        final TechnicalTag 리액트_태그 = technicalTagRepository.save(TechnicalTagFixture.createReact());

        final Supporter 서포터_디투 = supporterRepository.save(SupporterFixture.create(사용자_디투, List.of(자바_태그, 리액트_태그)));
        final String 서포터_디투_토큰 = login(디투_소셜_아이디);

        // when, then
        SupporterProfileAssuredSupport
                .클라이언트_요청()
                .로그인_한다(서포터_디투_토큰)
                .서포터_마이페이지를_토큰으로_조회한다()

                .서버_응답()
                .서포터_마이페이지_프로필_조회_성공을_검증한다(응답(서포터_디투));
    }

    private SupporterResponse.MyProfile 응답(final Supporter 서포터) {
        final Member 사용자_디투 = 서포터.getMember();
        return new SupporterResponse.MyProfile(
                사용자_디투.getMemberName().getValue(),
                사용자_디투.getImageUrl().getValue(),
                사용자_디투.getGithubUrl().getValue(),
                서포터.getIntroduction().getValue(),
                사용자_디투.getCompany().getValue(),
                서포터_기술_스택(서포터)
        );
    }

    private List<String> 서포터_기술_스택(final Supporter 서포터) {
        return 서포터.getSupporterTechnicalTags().getSupporterTechnicalTags().stream()
                .map(supporterTechnicalTag -> supporterTechnicalTag.getTechnicalTag().getTagName().getValue())
                .toList();
    }
}
