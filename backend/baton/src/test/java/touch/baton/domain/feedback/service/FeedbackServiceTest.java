package touch.baton.domain.feedback.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.feedback.exception.FeedbackBusinessException;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.tobe.domain.member.command.Member;
import touch.baton.tobe.domain.member.command.Runner;
import touch.baton.tobe.domain.member.command.Supporter;
import touch.baton.tobe.domain.runnerpost.command.RunnerPost;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class FeedbackServiceTest extends ServiceTestConfig {

    private FeedbackService feedbackService;
    private Runner exactRunner;
    private RunnerPost runnerPost;
    private SupporterFeedBackCreateRequest request;
    private Supporter reviewedSupporter;

    @BeforeEach
    void setUp() {
        feedbackService = new FeedbackService(supporterFeedbackRepository, runnerPostQueryRepository, supporterQueryRepository);
        final Member ethan = memberCommandRepository.save(MemberFixture.createEthan());
        exactRunner = runnerQueryRepository.save(RunnerFixture.createRunner(ethan));
        final Member ditoo = memberCommandRepository.save(MemberFixture.createDitoo());
        reviewedSupporter = supporterQueryRepository.save(SupporterFixture.create(ditoo));
        runnerPost = runnerPostQueryRepository.save(RunnerPostFixture.create(exactRunner, reviewedSupporter));

        request = new SupporterFeedBackCreateRequest("GOOD", List.of("코드리뷰가 맛있어요.", "말투가 친절해요."), reviewedSupporter.getId(), runnerPost.getId());
    }

    @DisplayName("러너가 서포터 피드백을 할 수 있다.")
    @Test
    void createSupporterFeedback() {
        // when
        final Long expected = feedbackService.createSupporterFeedback(exactRunner, request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(runnerPost.getIsReviewed().getValue()).isTrue();
        });
    }

    @DisplayName("소유자가 아닌 러너는 피드백을 할 수 없다.")
    @Test
    void fail_createSupporterFeedback_if_not_owner_runner() {
        // given
        final Member differentMember = memberCommandRepository.save(MemberFixture.createHyena());
        final Runner notOwner = runnerQueryRepository.save(RunnerFixture.createRunner(differentMember));

        // when, then
        assertThatThrownBy(() -> feedbackService.createSupporterFeedback(notOwner, request))
                .isInstanceOf(FeedbackBusinessException.class);
    }

    @DisplayName("리뷰를 하지 않은 서포터를 피드백을 할 수 없다.")
    @Test
    void fail_createSupporterFeedback_if_not_review_supporter_runner() {
        // given
        final Member differentMember = memberCommandRepository.save(MemberFixture.createHyena());
        final Supporter notReviewSupporter = supporterQueryRepository.save(SupporterFixture.create(differentMember));
        final SupporterFeedBackCreateRequest notReviewSupporterRequest = new SupporterFeedBackCreateRequest("GOOD", new ArrayList<>(), notReviewSupporter.getId(), runnerPost.getId());

        // when, then
        assertThatThrownBy(() -> feedbackService.createSupporterFeedback(exactRunner, notReviewSupporterRequest))
                .isInstanceOf(FeedbackBusinessException.class);
    }

    @DisplayName("이미 서포터 피드백을 작성했으면 서포터 피드백을 할 수 없다.")
    @Test
    void fail_createSupporterFeedback_if_already_reviewed_supporter() {
        // given
        final SupporterFeedBackCreateRequest supporterFeedBackCreateRequest = new SupporterFeedBackCreateRequest("GOOD", new ArrayList<>(), reviewedSupporter.getId(), runnerPost.getId());
        feedbackService.createSupporterFeedback(exactRunner, supporterFeedBackCreateRequest);

        // when, then
        assertThatThrownBy(() -> feedbackService.createSupporterFeedback(exactRunner, supporterFeedBackCreateRequest))
                .isInstanceOf(FeedbackBusinessException.class);
    }
}
