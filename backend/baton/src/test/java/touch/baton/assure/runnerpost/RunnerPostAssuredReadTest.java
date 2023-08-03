package touch.baton.assure.runnerpost;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.domain.common.vo.Grade;
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

import static touch.baton.fixture.vo.ChattingCountFixture.chattingCount;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;
import static touch.baton.fixture.vo.IntroductionFixture.introduction;
import static touch.baton.fixture.vo.TotalRatingFixture.totalRating;
import static touch.baton.fixture.vo.WatchedCountFixture.watchedCount;

@SuppressWarnings("NonAsciiCharacters")
class RunnerPostAssuredReadTest extends AssuredTestConfig {

    @Disabled
    @Test
    void 러너의_게시글_식별자값으로_러너_게시글_상세_정보_조회에_성공한다() {
        final Member memberHyena = memberRepository.save(MemberFixture.createHyena());
        final Runner runnerHyena = runnerRepository.save(RunnerFixture.create(totalRating(0), Grade.BARE_FOOT, introduction("안녕하세요"), memberHyena));
        final RunnerPost runnerPost = runnerPostRepository.save(RunnerPostFixture.create(runnerHyena, deadline(LocalDateTime.now().plusHours(100))));

        RunnerPostAssuredSupport
                .클라이언트_요청().러너_게시글_식별자값으로_러너_게시글을_조회한다(runnerPost.getId())
                .서버_응답().러너_게시글_단건_조회_성공을_검증한다(new RunnerPostResponse.Detail(
                        runnerPost.getId(),
                        runnerPost.getTitle().getValue(),
                        runnerPost.getContents().getValue(),
                        runnerPost.getPullRequestUrl().getValue(),
                        runnerPost.getDeadline().getValue(),
                        watchedCount(1).getValue(),
                        chattingCount(0).getValue(),
                        ReviewStatus.NOT_STARTED,
                        true,
                        RunnerResponse.Detail.from(runnerHyena),
                        Collections.emptyList()
                ));
    }
}
