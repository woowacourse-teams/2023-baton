package touch.baton.domain.runnerpost.repository.read;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.LongStream;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static touch.baton.domain.runnerpost.vo.ReviewStatus.NOT_STARTED;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;

class RunnerPostCustomRepositoryImplTest extends RepositoryTestConfig {

    @Autowired
    private RunnerPostRepository runnerPostRepository;

    @Autowired
    private EntityManager em;

    private Runner runner;

    @BeforeEach
    void setUp() {
        final Member ditoo = MemberFixture.createDitoo();
        em.persist(ditoo);
        runner = RunnerFixture.createRunner(ditoo);
        em.persist(runner);
    }

    @DisplayName("리뷰 상태로 러너 게시글을 페이지 조회한다")
    @Test
    void findByPageInfoAndReviewStatus() {
        // given
        final ReviewStatus reviewStatus = NOT_STARTED;
        for (int i = 0; i < 20; i++) {
            persistRunnerPost(runner, reviewStatus);
        }
        final Long previousLastId = 11L;
        final int limit = 10;

        // when
        final List<RunnerPost> runnerPosts = runnerPostRepository.findByPageInfoAndReviewStatus(previousLastId, limit, reviewStatus);
        final List<Long> expected = LongStream.rangeClosed(1L, 10L)
                .mapToObj(i -> 11L - i)
                .toList();

        // then
        assertSoftly(softly -> {
           softly.assertThat(runnerPosts).hasSize(10);
           softly.assertThat(runnerPosts.stream().mapToLong(RunnerPost::getId))
                   .isEqualTo(expected);
        });
    }

    private void persistRunnerPost(final Runner runner, final ReviewStatus reviewStatus) {
        final RunnerPost runnerPost = RunnerPostFixture.create(runner, deadline(LocalDateTime.now().plusHours(100)), reviewStatus);
        em.persist(runnerPost);
    }
}
