package touch.baton.common.schedule;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static touch.baton.domain.runnerpost.vo.ReviewStatus.*;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;

class ScheduleRunnerPostRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private ScheduleRunnerPostRepository scheduleRunnerPostRepository;

    @Autowired
    private EntityManager em;

    private Runner runner;

    @BeforeEach
    void setUp() {
        final Member member = MemberFixture.createDitoo();
        em.persist(member);
        runner = RunnerFixture.createRunner(member);
        em.persist(runner);
    }

    @DisplayName("deadline 이 지난 NOT_STARTED 상태의 runnerPost 는 OVERDUE 상태로 된다.")
    @Test
    void updateAllPassedDeadline_success() {
        // given
        final Deadline passedDeadlineOne = deadline(LocalDateTime.now().minusMinutes(1));
        final Deadline passedDeadlineTwo = deadline(LocalDateTime.now().minusMinutes(1));
        final RunnerPost runnerPostOne = RunnerPostFixture.create(runner, passedDeadlineOne);
        final RunnerPost runnerPostTwo = RunnerPostFixture.create(runner, passedDeadlineTwo);
        em.persist(runnerPostOne);
        em.persist(runnerPostTwo);

        // when
        scheduleRunnerPostRepository.updateAllPassedDeadline();
        final List<ReviewStatus> actual = scheduleRunnerPostRepository.findAll().stream()
                .map(RunnerPost::getReviewStatus)
                .toList();

        // then
        assertThat(actual).containsExactly(OVERDUE, OVERDUE);
    }

    @DisplayName("deadline 이 지난 DONE 상태의 runnerPost 는 리뷰 상태가 업데이트 되지 않는다.")
    @Test
    void updateAllPassedDeadline_fail_when_reviewStatus_is_DONE() {
        // given
        final Deadline passedDeadlineOne = deadline(LocalDateTime.now().minusHours(10));
        final Deadline passedDeadlineTwo = deadline(LocalDateTime.now().minusHours(5));
        final ReviewStatus expectedReviewStatus = DONE;
        final RunnerPost runnerPostOne = RunnerPostFixture.create(runner, passedDeadlineOne, expectedReviewStatus);
        final RunnerPost runnerPostTwo = RunnerPostFixture.create(runner, passedDeadlineTwo, expectedReviewStatus);
        em.persist(runnerPostOne);
        em.persist(runnerPostTwo);

        // when
        scheduleRunnerPostRepository.updateAllPassedDeadline();
        final List<ReviewStatus> actual = scheduleRunnerPostRepository.findAll().stream()
                .map(RunnerPost::getReviewStatus)
                .toList();

        // then
        assertThat(actual).containsExactly(expectedReviewStatus, expectedReviewStatus);
    }

    @DisplayName("deadline 이 지나지 않은 NOT_STARTED 상태의 runnerPost 는 리뷰 상태가 업데이트 되지 않는다.")
    @Test
    void updateAllPassedDeadline_fail_when_deadline_is_not_passed() {
        // given
        final Deadline passedDeadlineOne = deadline(LocalDateTime.now().plusHours(10));
        final Deadline passedDeadlineTwo = deadline(LocalDateTime.now().plusHours(5));
        final ReviewStatus expectedReviewStatus = NOT_STARTED;
        final RunnerPost runnerPostOne = RunnerPostFixture.create(runner, passedDeadlineOne, expectedReviewStatus);
        final RunnerPost runnerPostTwo = RunnerPostFixture.create(runner, passedDeadlineTwo, expectedReviewStatus);
        em.persist(runnerPostOne);
        em.persist(runnerPostTwo);

        // when
        scheduleRunnerPostRepository.updateAllPassedDeadline();
        final List<ReviewStatus> actual = scheduleRunnerPostRepository.findAll().stream()
                .map(RunnerPost::getReviewStatus)
                .toList();

        // then
        assertThat(actual).containsExactly(expectedReviewStatus, expectedReviewStatus);
    }
}
