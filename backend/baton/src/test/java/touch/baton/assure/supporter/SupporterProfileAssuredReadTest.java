package touch.baton.assure.supporter;

import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.supporter.Supporter;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.SupporterFixture;

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
}
