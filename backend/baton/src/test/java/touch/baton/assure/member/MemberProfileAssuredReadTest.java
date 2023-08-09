package touch.baton.assure.member;

import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.fixture.domain.MemberFixture;

import static touch.baton.assure.member.MemberProfileAssuredSupport.로그인_한_맴버_프로필_응답;

@SuppressWarnings("NonAsciiCharacters")
public class MemberProfileAssuredReadTest extends AssuredTestConfig {

    @Test
    void 로그인_한_맴버_프로필을_조회한다() {
        final String 디투_소셜_id = "ditooSocialId";
        final Member 사용자_디투 = memberRepository.save(MemberFixture.createWithSocialId(디투_소셜_id));
        final String 디투_액세스_토큰 = login(디투_소셜_id);

        MemberProfileAssuredSupport
                .클라이언트_요청()
                .로그인_유저_프로필을_액세스_토큰으로_조회한다(디투_액세스_토큰)
                .서버_응답()
                .맴버_로그인_프로필_조회_성공을_검증한다(로그인_한_맴버_프로필_응답(사용자_디투));
    }
}
