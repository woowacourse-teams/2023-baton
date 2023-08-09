package touch.baton.assure.runner;


import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.controller.response.RunnerProfileResponse;
import touch.baton.domain.technicaltag.TechnicalTag;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.TechnicalTagFixture;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
class RunnerProfileControllerTest extends AssuredTestConfig {

    @Test
    void 러너_프로필_조회에_성공한다() {
        // given
        final Member member = memberRepository.save(MemberFixture.createEthan());
        final TechnicalTag javaTag = technicalTagRepository.save(TechnicalTagFixture.createJava());
        final TechnicalTag reactTag = technicalTagRepository.save(TechnicalTagFixture.createReact());
        final Runner savedRunner = runnerRepository.save(RunnerFixture.createRunner(member, List.of(javaTag, reactTag)));

        // when, then
        RunnerProfileAssuredSupport
                .클라이언트_요청().러너_피드백을_상세_조회한다(savedRunner.getId())
                .서버_응답().러너_프로필_상세_조회를_검증한다(new RunnerProfileResponse.Detail(
                        1L,
                        "에단",
                        "https://",
                        "https://github.com/",
                        "안녕하세요.",
                        "우아한테크코스 5기 백엔드",
                        List.of("Java", "React")));
    }
}
