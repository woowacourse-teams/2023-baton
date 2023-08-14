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
class RunnerReadByRunnerIdAssuredTest extends AssuredTestConfig {

    @Test
    void 러너_프로필_조회에_성공한다() {
        // given
        final Member 사용자_에단 = memberRepository.save(MemberFixture.createEthan());
        final TechnicalTag 자바_태그 = technicalTagRepository.save(TechnicalTagFixture.createJava());
        final TechnicalTag 리액트_태그 = technicalTagRepository.save(TechnicalTagFixture.createReact());
        final Runner 러너_에단 = runnerRepository.save(RunnerFixture.createRunner(사용자_에단, List.of(자바_태그, 리액트_태그)));
        final RunnerProfileResponse.Detail 러너_프로필_조회_응답 = new RunnerProfileResponse.Detail(
                1L,
                "에단",
                "https://",
                "https://github.com/",
                "안녕하세요.",
                "우아한테크코스 5기 백엔드",
                List.of("Java", "React"));

        // when, then
        RunnerAssuredSupport
                .클라이언트_요청()
                .러너_프로필을_상세_조회한다(러너_에단.getId())

                .서버_응답()
                .러너_프로필_상세_조회를_검증한다(러너_프로필_조회_응답);
    }
}
