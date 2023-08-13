package touch.baton.domain.supporter.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.SupporterRunnerPost;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.SupporterRunnerPostFixture;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class SupporterRunnerPostRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private SupporterRunnerPostRepository supporterRunnerPostRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("서포터의_러너_게시글_리뷰_제안을_철회하는데_성공한다")
    @Test
    void deleteBySupporterAndRunnerPostId() {
        // given
        final Member reviewerMember = MemberFixture.createDitoo();
        entityManager.persist(reviewerMember);
        final Supporter reviewerSupporter = SupporterFixture.create(reviewerMember);
        entityManager.persist(reviewerSupporter);

        final Member revieweeMember = MemberFixture.createJudy();
        entityManager.persist(revieweeMember);
        final Runner revieweeRunner = RunnerFixture.createRunner(revieweeMember);
        entityManager.persist(revieweeRunner);
        final RunnerPost runnerPost = RunnerPostFixture.create(revieweeRunner, reviewerSupporter, new Deadline(LocalDateTime.now().plusHours(100)));
        entityManager.persist(runnerPost);

        final SupporterRunnerPost deletedSupporterRunnerPost = SupporterRunnerPostFixture.create(reviewerSupporter, runnerPost);
        entityManager.persist(deletedSupporterRunnerPost);

        // when
        final int deleteCount = supporterRunnerPostRepository.deleteBySupporterIdAndRunnerPostId(reviewerSupporter.getId(), runnerPost.getId());
        entityManager.flush();

        // then
        assertSoftly(softly -> {
            softly.assertThat(deleteCount).isEqualTo(1);
            softly.assertThat(supporterRunnerPostRepository.findById(deletedSupporterRunnerPost.getId())).isNotPresent();
        });
    }

    @DisplayName("서포터의_러너_게시글_리뷰_제안을_철회하는데_리뷰_제안을_한적이_없으면_실패한다")
    @Test
    void deleteBySupporterAndRunnerPostId_fail() {
        // given
        final Member reviewerMember = MemberFixture.createDitoo();
        entityManager.persist(reviewerMember);
        final Supporter reviewerSupporter = SupporterFixture.create(reviewerMember);
        entityManager.persist(reviewerSupporter);

        final Member revieweeMember = MemberFixture.createJudy();
        entityManager.persist(revieweeMember);
        final Runner revieweeRunner = RunnerFixture.createRunner(revieweeMember);
        entityManager.persist(revieweeRunner);
        final RunnerPost runnerPost = RunnerPostFixture.create(revieweeRunner, reviewerSupporter, new Deadline(LocalDateTime.now().plusHours(100)));
        entityManager.persist(runnerPost);

        // when
        final int deleteCount = supporterRunnerPostRepository.deleteBySupporterIdAndRunnerPostId(reviewerSupporter.getId(), runnerPost.getId());
        entityManager.flush();

        // then
        assertThat(deleteCount).isZero();
    }
}
