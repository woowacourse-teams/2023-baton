package touch.baton.assure.runnerpost;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.RunnerPostTagsFixture;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static touch.baton.domain.runnerpost.vo.ReviewStatus.NOT_STARTED;
import static touch.baton.fixture.vo.ContentsFixture.contents;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;
import static touch.baton.fixture.vo.IntroductionFixture.introduction;
import static touch.baton.fixture.vo.PullRequestUrlFixture.pullRequestUrl;
import static touch.baton.fixture.vo.TitleFixture.title;
import static touch.baton.fixture.vo.WatchedCountFixture.watchedCount;

@SuppressWarnings("NonAsciiCharacters")
class RunnerPostDeleteAssuredTest extends AssuredTestConfig {

    @Disabled
    @Test
    void 러너의_게시글_식별자값으로_러너_게시글_상세_정보_삭제에_성공한다() {
        final Member member = MemberFixture.createHyena();
        memberRepository.save(member);

        final Runner runner = RunnerFixture.createRunner(introduction("hello"), member);
        runnerRepository.save(runner);

        final RunnerPost runnerPost = RunnerPostFixture.create(title("제 코드를 리뷰해주세요"),
                contents("제 코드의 내용은 이렇습니다."),
                pullRequestUrl("https://"),
                deadline(LocalDateTime.now().plusHours(10)),
                watchedCount(0),
                NOT_STARTED,
                runner,
                null,
                RunnerPostTagsFixture.runnerPostTags(new ArrayList<>()));
        runnerPostRepository.save(runnerPost);

        RunnerPostAssuredSupport
                .클라이언트_요청().러너_게시글_식별자값으로_러너_게시글을_삭제한다(runnerPost.getId())
                .서버_응답().러너_게시글_삭제_성공을_검증한다(NO_CONTENT);
    }
}
