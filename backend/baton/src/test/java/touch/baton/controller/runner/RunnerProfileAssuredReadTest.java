package touch.baton.controller.runner;

import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;

@SuppressWarnings("NonAsciiCharacters")
public class RunnerProfileAssuredReadTest extends AssuredTestConfig {

    @Test
    void 액세스_토큰으로_러너_본인의_정보_조회에_성공한다() {
        // TODO: Access Token 으로 바꿔야함
//        final String accessToken = "abc123";
//        final Member memberHyena = memberRepository.save(MemberFixture.createHyena());
//        final Runner runnerHyena = runnerRepository.save(RunnerFixture.create(totalRating(0), Grade.BARE_FOOT, introduction("hello"), memberHyena));
//        final RunnerPost runnerPost = runnerPostRepository.save(RunnerPostFixture.create(runnerHyena, deadline(LocalDateTime.now().plusHours(100))));
//
//        RunnerAssuredSupport
//                .클라이언트_요청().러너_액세스_토큰으로_러너_프로필을_조회한다(accessToken)
//                .서버_응답().러너_본인_프로필_조회_성공을_검증한다(new RunnerMyProfileResponse(
//                        RunnerResponse.Mine.from(runnerHyena),
//                        List.of(RunnerPostResponse.Mine.from(runnerPost)
//                )));
    }
}
