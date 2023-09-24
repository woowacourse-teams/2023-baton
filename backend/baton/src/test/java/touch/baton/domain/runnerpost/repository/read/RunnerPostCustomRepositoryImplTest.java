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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    // FIXME: 다른 테스트랑 데이터 겹침
    @DisplayName("리뷰 상태로 러너 게시글을 페이지 조회한다 (2 페이지 이상 조회)")
    @Test
    void findByPageInfoAndReviewStatus_secondPage() {
        // given
        final ReviewStatus reviewStatus = NOT_STARTED;
        final List<Long> runnerPostIds = new ArrayList<>();
        final int persistSize = 30;
        for (int i = 0; i < persistSize; i++) {
            runnerPostIds.add(persistRunnerPost(runner, reviewStatus));
        }
        final int lastIndex = persistSize - 1;
        final Long previousLastId = runnerPostIds.get(lastIndex);
        final int limit = 10;

        // when
        final List<RunnerPost> runnerPosts = runnerPostRepository.findByPageInfoAndReviewStatus(previousLastId, limit, reviewStatus);
        runnerPostIds.sort(Comparator.reverseOrder());
        final List<Long> expected = runnerPostIds.subList(1, 1 + limit);

        // then
        assertSoftly(softly -> {
           softly.assertThat(runnerPosts).hasSize(limit);
           softly.assertThat(runnerPosts.stream().mapToLong(RunnerPost::getId))
                   .isEqualTo(expected);
        });
    }

    // FIXME: 다른 테스트랑 데이터 겹침
    @DisplayName("리뷰 상태로 러너 게시글을 페이지 조회한다 (첫 페이지 조회)")
    @Test
    void findByPageInfoAndReviewStatus_firstPage() {
        // given
        final ReviewStatus reviewStatus = NOT_STARTED;
        final int runnerPostCount = 10;
        final List<Long> runnerPostIds = new ArrayList<>();
        for (int i = 0; i < runnerPostCount; i++) {
            runnerPostIds.add(persistRunnerPost(runner, reviewStatus));
        }
        final int limit = 10;

        // when
        final List<RunnerPost> runnerPosts = runnerPostRepository.findLatestByLimitAndReviewStatus(limit, reviewStatus);
        runnerPostIds.sort(Comparator.reverseOrder());
        final List<Long> expected = runnerPostIds;

        // then
        assertSoftly(softly -> {
            softly.assertThat(runnerPosts).hasSize(limit);
            softly.assertThat(runnerPosts.stream().mapToLong(RunnerPost::getId))
                    .isEqualTo(expected);
        });
    }

    private Long persistRunnerPost(final Runner runner, final ReviewStatus reviewStatus) {
        final RunnerPost runnerPost = RunnerPostFixture.create(runner, deadline(LocalDateTime.now().plusHours(100)), reviewStatus);
        em.persist(runnerPost);
        return runnerPost.getId();
    }
}
