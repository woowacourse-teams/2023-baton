package touch.baton.domain.supporter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import touch.baton.domain.feedback.exception.SupporterFeedbackException;
import touch.baton.domain.feedback.vo.Description;
import touch.baton.domain.feedback.vo.ReviewType;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.supporter.vo.ReviewCount;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.RunnerPostTagsFixture;
import touch.baton.fixture.domain.SupporterFixture;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static touch.baton.domain.feedback.SupporterFeedback.builder;
import static touch.baton.domain.runnerpost.vo.ReviewStatus.NOT_STARTED;
import static touch.baton.fixture.vo.CuriousContentsFixture.curiousContents;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;
import static touch.baton.fixture.vo.ImplementedContentsFixture.implementedContents;
import static touch.baton.fixture.vo.PostscriptContentsFixture.postscriptContents;
import static touch.baton.fixture.vo.PullRequestUrlFixture.pullRequestUrl;
import static touch.baton.fixture.vo.TitleFixture.title;
import static touch.baton.fixture.vo.WatchedCountFixture.watchedCount;

;

class SupporterFeedbackTest {

    @DisplayName("생성 테스트")
    @Nested
    class Create {

        private Supporter supporter;
        private Runner runner;
        private RunnerPost runnerPost;

        @BeforeEach
        void setUp() {
            supporter = SupporterFixture.create(new ReviewCount(0),
                    MemberFixture.createEthan(),
                    new ArrayList<>());

            runner = RunnerFixture.createRunner(MemberFixture.createDitoo());

            runnerPost = RunnerPostFixture.create(title("제 코드를 리뷰해주세요"),
                    implementedContents("제 코드의 내용은 이렇습니다."),
                    curiousContents("제 궁금증은 이렇습니다."),
                    postscriptContents("제 참고 사항은 이렇습니다."),
                    pullRequestUrl("https://"),
                    deadline(LocalDateTime.now().plusHours(10)),
                    watchedCount(0),
                    NOT_STARTED,
                    runner,
                    supporter,
                    RunnerPostTagsFixture.runnerPostTags(new ArrayList<>()));
        }

        @DisplayName("성공한다.")
        @Test
        void success() {
            // when, then
            assertThatCode(() -> builder()
                    .reviewType(ReviewType.GOOD)
                    .description(new Description("무난무난"))
                    .supporter(supporter)
                    .runner(runner)
                    .runnerPost(runnerPost)
                    .build()
            ).doesNotThrowAnyException();
        }

        @DisplayName("reviewType 이 null 이면 실패한다.")
        @Test
        void fail_if_reviewType_is_null() {
            // when, then
            assertThatThrownBy(() -> builder()
                    .reviewType(null)
                    .description(new Description("무난무난"))
                    .supporter(supporter)
                    .runner(runner)
                    .runnerPost(runnerPost)
                    .build()
            ).isInstanceOf(SupporterFeedbackException.class)
                    .hasMessage("SupporterFeedback 의 reviewType 은 null 일 수 없습니다.");
        }

        @DisplayName("description 이 null 이면 실패한다.")
        @Test
        void fail_if_description_is_null() {
            // when, then
            assertThatThrownBy(() -> builder()
                    .reviewType(ReviewType.GOOD)
                    .description(null)
                    .supporter(supporter)
                    .runner(runner)
                    .runnerPost(runnerPost)
                    .build()
            ).isInstanceOf(SupporterFeedbackException.class)
                    .hasMessage("SupporterFeedback 의 description 은 null 일 수 없습니다.");
        }

        @DisplayName("supporter 가 null 이면 실패한다.")
        @Test
        void fail_if_supporter_is_null() {
            // when, then
            assertThatThrownBy(() -> builder()
                    .reviewType(ReviewType.GOOD)
                    .description(new Description("무난무난"))
                    .supporter(null)
                    .runner(runner)
                    .runnerPost(runnerPost)
                    .build()
            ).isInstanceOf(SupporterFeedbackException.class)
                    .hasMessage("SupporterFeedback 의 supporter 는 null 일 수 없습니다.");
        }

        @DisplayName("runner 가 null 이면 실패한다.")
        @Test
        void fail_if_runner_is_null() {
            // when, then
            assertThatThrownBy(() -> builder()
                    .reviewType(ReviewType.GOOD)
                    .description(new Description("무난무난"))
                    .supporter(supporter)
                    .runner(null)
                    .runnerPost(runnerPost)
                    .build()
            ).isInstanceOf(SupporterFeedbackException.class)
                    .hasMessage("SupporterFeedback 의 runner 는 null 일 수 없습니다.");
        }

        @DisplayName("runnerPost 가 null 이면 실패한다.")
        @Test
        void fail_if_runnerPost_is_null() {
            // when, then
            assertThatThrownBy(() -> builder()
                    .reviewType(ReviewType.GOOD)
                    .description(new Description("무난무난"))
                    .supporter(supporter)
                    .runner(runner)
                    .runnerPost(null)
                    .build()
            ).isInstanceOf(SupporterFeedbackException.class)
                    .hasMessage("SupporterFeedback 의 runnerPost 는 null 일 수 없습니다.");
        }
    }
}
