package touch.baton.domain.feedback.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.feedback.exception.FeedbackBusinessException;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.supporter.Supporter;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FeedbackServiceTest extends ServiceTestConfig {

    private FeedbackService feedbackService;
    private Member ethan;
    private Runner exactRunner;
    private Member ditoo;
    private Supporter supporterDitoo;
    private RunnerPost runnerPost;
    private SupporterFeedBackCreateRequest request;

    @BeforeEach
    void setUp() {
        feedbackService = new FeedbackService(supporterFeedbackRepository, runnerRepository, runnerPostRepository, supporterRepository);
        ethan = memberRepository.save(MemberFixture.createEthan());
        exactRunner = runnerRepository.save(RunnerFixture.createRunner(ethan));
        ditoo = memberRepository.save(MemberFixture.createDitoo());
        supporterDitoo = supporterRepository.save(SupporterFixture.create(ditoo));
        runnerPost = runnerPostRepository.save(RunnerPostFixture.create(exactRunner, supporterDitoo));

        request = new SupporterFeedBackCreateRequest("GOOD", List.of("코드리뷰가 맛있어요.", "말투가 친절해요."), supporterDitoo.getId(), runnerPost.getId());
    }

    @DisplayName("러너가 서포터 피드백을 할 수 있다.")
    @Test
    void createSupporterFeedback() {
        // when
        final Long expected = feedbackService.createSupporterFeedback(exactRunner, request);

        // then
        assertThat(expected).isNotNull();
    }

    @DisplayName("소유자가 아닌 러너는 피드백을 할 수 없다.")
    @Test
    void fail_createSupporterFeedback_if_not_owner_runner() {
        // given
        final Member differentMember = memberRepository.save(MemberFixture.createHyena());
        final Runner notOwner = runnerRepository.save(RunnerFixture.createRunner(differentMember));

        // when, then
        assertThatThrownBy(() -> feedbackService.createSupporterFeedback(notOwner, request))
                .isInstanceOf(FeedbackBusinessException.class)
                .hasMessage("리뷰 글을 작성한 주인만 글을 작성할 수 있습니다.");
    }
    
    @DisplayName("리뷰를 하지 않은 서포터를 피드백을 할 수 없다.")
    @Test
    void fail_createSupporterFeedback_if_not_review_supporter_runner() {
        // given
        final Member differentMember = memberRepository.save(MemberFixture.createHyena());
        final Supporter notReviewSupporter = supporterRepository.save(SupporterFixture.create(differentMember));
        final SupporterFeedBackCreateRequest notReviewSupporterRequest = new SupporterFeedBackCreateRequest("GOOD", new ArrayList<>(), notReviewSupporter.getId(), runnerPost.getId());

        // when, then
        assertThatThrownBy(() -> feedbackService.createSupporterFeedback(exactRunner, notReviewSupporterRequest))
                .isInstanceOf(FeedbackBusinessException.class)
                .hasMessage("리뷰를 작성한 서포터에 대해서만 피드백을 작성할 수 있습니다.");
    }
}
