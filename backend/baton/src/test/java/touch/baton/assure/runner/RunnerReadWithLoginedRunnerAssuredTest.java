package touch.baton.assure.runner;

import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;

import static touch.baton.assure.runner.RunnerAssuredSupport.러너_본인_프로필_응답;
import static touch.baton.fixture.vo.IntroductionFixture.introduction;

@SuppressWarnings("NonAsciiCharacters")
class RunnerReadWithLoginedRunnerAssuredTest extends AssuredTestConfig {

    @Test
    void 러너_본인_프로필을_가지고_있는_액세스_토큰으로_조회에_성공한다() {
        final Member 사용자_헤나 = memberRepository.save(MemberFixture.createHyena());
        final Runner 러너_헤나 = runnerRepository.save(RunnerFixture.createRunner(introduction("안녕하세요"), 사용자_헤나));
        final String 로그인용_액세스_토큰 = login(사용자_헤나.getSocialId().getValue());

        RunnerAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(로그인용_액세스_토큰)
                .러너_본인_프로필을_가지고_있는_액세스_토큰으로_조회한다()

                .서버_응답()
                .러너_본인_프로필_조회_성공을_검증한다(러너_본인_프로필_응답(러너_헤나));
    }
}
