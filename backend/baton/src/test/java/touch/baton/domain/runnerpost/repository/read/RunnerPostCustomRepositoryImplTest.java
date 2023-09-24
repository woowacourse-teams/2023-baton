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

    private static final Long GENERATED_START_ID = 1L;

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

    @DisplayName("리뷰 상태로 러너 게시글을 페이지 조회한다 (2페이지 조회)")
    @Test
    void findByPageInfoAndReviewStatus_secondPage() {
        // given
        final ReviewStatus reviewStatus = NOT_STARTED;
        for (int i = 0; i < 20; i++) {
            persistRunnerPost(runner, reviewStatus);
        }
        final long previousLastId = 11L;
        final int limit = 10;

        // when
        final List<RunnerPost> runnerPosts = runnerPostRepository.findByPageInfoAndReviewStatus(previousLastId, limit, reviewStatus);
        final List<Long> expected = LongStream.range(GENERATED_START_ID, GENERATED_START_ID + limit)
                .mapToObj(i -> previousLastId - i)
                .toList();

        // then
        assertSoftly(softly -> {
           softly.assertThat(runnerPosts).hasSize(limit);
           softly.assertThat(runnerPosts.stream().mapToLong(RunnerPost::getId))
                   .isEqualTo(expected);
        });
    }

    @DisplayName("리뷰 상태로 러너 게시글을 페이지 조회한다 (첫 페이지 조회)")
    @Test
    void findByPageInfoAndReviewStatus_firstPage() {
        // given
        final ReviewStatus reviewStatus = NOT_STARTED;
        final int runnerPostCount = 20;
        for (int i = 0; i < runnerPostCount; i++) {
            persistRunnerPost(runner, reviewStatus);
        }
        final int limit = 10;

        // when
        final List<RunnerPost> runnerPosts = runnerPostRepository.findLatestByLimitAndReviewStatus(limit, reviewStatus);
        final List<Long> expected = LongStream.range(GENERATED_START_ID, GENERATED_START_ID + limit)
                .mapToObj(i -> runnerPostCount + 1 - i)
                .toList();

        // then
        assertSoftly(softly -> {
            softly.assertThat(runnerPosts).hasSize(limit);
            softly.assertThat(runnerPosts.stream().mapToLong(RunnerPost::getId))
                    .isEqualTo(expected);
        });
    }

    private void persistRunnerPost(final Runner runner, final ReviewStatus reviewStatus) {
        final RunnerPost runnerPost = RunnerPostFixture.create(runner, deadline(LocalDateTime.now().plusHours(100)), reviewStatus);
        em.persist(runnerPost);
    }
}
