package touch.baton.domain.feedback.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.feedback.SupporterFeedback;
import touch.baton.tobe.domain.member.command.Member;
import touch.baton.tobe.domain.member.command.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.tobe.domain.member.command.Supporter;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFeedbackFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.vo.DeadlineFixture;

import java.time.LocalDateTime;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class SupporterFeedbackRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private EntityManager em;
    @Autowired
    private SupporterFeedbackRepository supporterFeedbackRepository;

    @DisplayName("러너 게시글 아이디와 서포터 아이디로 서포터 피드백 존재 유무를 확인할 수 있다.")
    @Test
    void existsByRunnerPostIdAndSupporterId() {
        // given
        final Member runnerMember = MemberFixture.createEthan();
        em.persist(runnerMember);
        final Runner runner = RunnerFixture.createRunner(runnerMember);
        em.persist(runner);

        final Member reviewedSupporterMember = MemberFixture.createHyena();
        em.persist(reviewedSupporterMember);
        final Supporter reviewedSupporter = SupporterFixture.create(reviewedSupporterMember);
        em.persist(reviewedSupporter);

        final RunnerPost runnerPost = RunnerPostFixture.create(runner, DeadlineFixture.deadline(LocalDateTime.now().plusDays(10)));
        em.persist(runnerPost);

        final SupporterFeedback supporterFeedback = SupporterFeedbackFixture.create(reviewedSupporter, runner, runnerPost);
        em.persist(supporterFeedback);

        final Member notReviewedSupporterMember = MemberFixture.createHyena();
        em.persist(notReviewedSupporterMember);
        final Supporter notReviewedSupporter = SupporterFixture.create(notReviewedSupporterMember);
        em.persist(notReviewedSupporter);

        em.flush();
        em.close();

        // when, then
        assertSoftly(softly -> {
            softly.assertThat(supporterFeedbackRepository.existsByRunnerPostIdAndSupporterId(runnerPost.getId(), reviewedSupporter.getId())).isTrue();
            softly.assertThat(supporterFeedbackRepository.existsByRunnerPostIdAndSupporterId(runnerPost.getId(), notReviewedSupporter.getId())).isFalse();
        });
    }
}
