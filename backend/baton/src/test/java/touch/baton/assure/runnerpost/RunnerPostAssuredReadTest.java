package touch.baton.assure.runnerpost;

import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.controller.response.RunnerResponse;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;

import java.time.LocalDateTime;
import java.util.Collections;

import static touch.baton.fixture.vo.DeadlineFixture.deadline;
import static touch.baton.fixture.vo.IntroductionFixture.introduction;
import static touch.baton.fixture.vo.WatchedCountFixture.watchedCount;

@SuppressWarnings("NonAsciiCharacters")
class RunnerPostAssuredReadTest extends AssuredTestConfig {

    @Test
    void 러너의_게시글_식별자값으로_러너_게시글_상세_정보_조회에_성공한다() {
        final Member 사용자_헤나 = memberRepository.save(MemberFixture.createHyena());
        final Runner 러너_헤나 = runnerRepository.save(RunnerFixture.create(introduction("안녕하세요"), 사용자_헤나));
        final RunnerPost 러너_게시글 = runnerPostRepository.save(RunnerPostFixture.create(러너_헤나, deadline(LocalDateTime.now().plusHours(100))));
        final String 로그인용_토큰 = login(사용자_헤나.getSocialId().getValue());

        RunnerPostAssuredSupport
                .클라이언트_요청()
                .토큰으로_로그인한다(로그인용_토큰)
                .러너_게시글_식별자값으로_러너_게시글을_조회한다(러너_게시글.getId())

                .서버_응답()
                .러너_게시글_단건_조회_성공을_검증한다(new RunnerPostResponse.Detail(
                        러너_게시글.getId(),
                        러너_게시글.getTitle().getValue(),
                        러너_게시글.getContents().getValue(),
                        러너_게시글.getPullRequestUrl().getValue(),
                        러너_게시글.getDeadline().getValue(),
                        watchedCount(1).getValue(),
                        ReviewStatus.NOT_STARTED,
                        true,
                        RunnerResponse.Detail.from(러너_헤나),
                        Collections.emptyList()
                ));
    }
}
