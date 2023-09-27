package touch.baton.common.schedule;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.vo.Deadline;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static touch.baton.domain.runnerpost.command.vo.ReviewStatus.OVERDUE;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;

class RunnerPostDeadlineCheckSchedulerTest extends ServiceTestConfig {

    @Autowired
    private ScheduleRunnerPostRepository scheduleRunnerPostRepository;

    private RunnerPostDeadlineCheckScheduler runnerPostDeadlineCheckScheduler;

    @BeforeEach
    void setUp() {
        runnerPostDeadlineCheckScheduler = new RunnerPostDeadlineCheckScheduler(scheduleRunnerPostRepository);
    }

    @Autowired
    private EntityManager em;

    @DisplayName("1분 전에 deadline 이 지난 runnerPost 는 OVERDUE 된다.")
    @Test
    void runnerPost_which_passed_deadline_might_be_overdue() {
        // given
        final Member member = MemberFixture.createEthan();
        em.persist(member);
        final Runner runner = RunnerFixture.createRunner(member);
        em.persist(runner);
        final Deadline passedDeadline = deadline(LocalDateTime.now().minusMinutes(1));
        final RunnerPost runnerPost = RunnerPostFixture.create(runner, passedDeadline);
        em.persist(runnerPost);

        // when
        runnerPostDeadlineCheckScheduler.updateReviewStatus();
        final RunnerPost actual = em.createQuery("select rp from RunnerPost rp", RunnerPost.class)
                .getSingleResult();

        // then
        assertThat(actual.getReviewStatus()).isEqualTo(OVERDUE);
    }
}
