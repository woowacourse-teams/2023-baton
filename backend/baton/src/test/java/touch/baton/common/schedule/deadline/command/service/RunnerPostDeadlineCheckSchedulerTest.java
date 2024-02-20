package touch.baton.common.schedule.deadline.command.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.common.fixture.DeadlineOutboxFixture;
import touch.baton.common.schedule.deadline.command.repository.DeadlineOutboxCommandRepository;
import touch.baton.common.schedule.deadline.command.repository.RunnerPostDeadlineCommandRepository;
import touch.baton.common.schedule.deadline.query.repository.RunnerPostDeadlineQueryRepository;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.command.vo.ReviewCount;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.vo.Deadline;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.vo.ReviewCountFixture;

import java.time.Instant;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static touch.baton.domain.runnerpost.command.vo.ReviewStatus.DONE;
import static touch.baton.domain.runnerpost.command.vo.ReviewStatus.OVERDUE;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;

class RunnerPostDeadlineCheckSchedulerTest extends ServiceTestConfig {

    @Autowired
    private RunnerPostDeadlineCommandRepository runnerPostDeadlineCommandRepository;

    @Autowired
    private RunnerPostDeadlineQueryRepository runnerPostDeadlineQueryRepository;

    @Autowired
    private DeadlineOutboxCommandRepository deadlineOutboxCommandRepository;

    private RunnerPostDeadlineCheckScheduler runnerPostDeadlineCheckScheduler;

    @BeforeEach
    void setUp() {
        runnerPostDeadlineCheckScheduler = new RunnerPostDeadlineCheckScheduler(runnerPostDeadlineCommandRepository, runnerPostDeadlineQueryRepository, deadlineOutboxCommandRepository);
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

    @DisplayName("RunnerPost를 조회할 때 Supporter를 join해서 조회한다.")
    @Test
    void joinBySupporter() {
        // given
        final Supporter supporter = persistSupporter(MemberFixture.createEthan());
        final Runner runner = persistRunner(MemberFixture.createHyena());
        final RunnerPost runnerPost = persistRunnerPost(RunnerPostFixture.create(runner, supporter));

        // when
        final RunnerPost actual = runnerPostDeadlineCheckScheduler.joinBySupporter(runnerPost.getId());

        // then
        assertThat(actual).isEqualTo(runnerPost);
    }

    @DisplayName("finishReview를 호출하면 RunnerPost의 reviewStatus가 DONE으로 변경되고, DeadlineOutbox가 삭제된다.")
    @Test
    void finishReview() {
        // given
        final Runner runner = persistRunner(MemberFixture.createHyena());
        final RunnerPost runnerPost = persistRunnerPost(runner);
        final Supporter supporter = persistSupporter(MemberFixture.createEthan());
        persistAssignSupporter(supporter, runnerPost);

        deadlineOutboxCommandRepository.save(DeadlineOutboxFixture.deadlineOutbox(runnerPost.getId(), Instant.now()));

        final ReviewCount originReviewCount = ReviewCountFixture.reviewCount(supporter.getReviewCount().getValue());

        // when
        runnerPostDeadlineCheckScheduler.finishReview(runnerPost.getId());

        // then
        assertAll(
                () -> assertThat(runnerPost.getSupporter().getReviewCount()).isEqualTo(ReviewCountFixture.reviewCount(originReviewCount.getValue() + 1)),
                () -> assertThat(runnerPost.getReviewStatus()).isEqualTo(DONE),
                () -> assertThat(deadlineOutboxCommandRepository.findAll()).isEmpty()
        );
    }
}
