package touch.baton.tobe.domain.runnerpost.command.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.ServiceTestConfig;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.SupporterRunnerPostFixture;
import touch.baton.tobe.domain.member.command.Member;
import touch.baton.tobe.domain.member.command.Runner;
import touch.baton.tobe.domain.member.command.Supporter;
import touch.baton.tobe.domain.member.command.SupporterRunnerPost;
import touch.baton.tobe.domain.runnerpost.command.RunnerPost;
import touch.baton.tobe.domain.runnerpost.command.exception.RunnerPostBusinessException;
import touch.baton.tobe.domain.runnerpost.command.vo.Deadline;
import touch.baton.tobe.domain.runnerpost.command.vo.IsReviewed;
import touch.baton.tobe.domain.runnerpost.command.vo.ReviewStatus;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RunnerPostUpdateApplicantCancelationServiceTest extends ServiceTestConfig {

    private RunnerPostCommandService runnerPostCommandService;

    private Supporter applicantSupporter;
    private Runner revieweeRunner;

    @BeforeEach
    void setUp() {
        runnerPostCommandService = new RunnerPostCommandService(
                runnerPostCommandRepository,
                tagCommandRepository,
                supporterCommandRepository,
                supporterRunnerPostCommandRepository
        );

        final Member applicantMember = memberCommandRepository.save(MemberFixture.createDitoo());
        applicantSupporter = supporterQueryRepository.save(SupporterFixture.create(applicantMember));

        final Member revieweeMember = memberCommandRepository.save(MemberFixture.createJudy());
        revieweeRunner = runnerQueryRepository.save(RunnerFixture.createRunner(revieweeMember));
    }

    @DisplayName("성공한다.")
    @Test
    void success() {
        // given
        final RunnerPost runnerPost = runnerPostQueryRepository.save(
                RunnerPostFixture.create(
                        revieweeRunner,
                        applicantSupporter,
                        new Deadline(LocalDateTime.now().plusHours(100))
                ));
        final SupporterRunnerPost supporterRunnerPost = SupporterRunnerPostFixture.create(runnerPost, applicantSupporter);
        supporterRunnerPostQueryRepository.save(supporterRunnerPost);

        // when
        runnerPostCommandService.deleteSupporterRunnerPost(applicantSupporter, runnerPost.getId());

        // then
        assertThat(supporterRunnerPostQueryRepository.findById(supporterRunnerPost.getId())).isNotPresent();
    }

    @DisplayName("RunnerPost 가 존재하지 않으면 실패한다.")
    @Test
    void fail_when_runnerPost_not_found() {
        // given
        final RunnerPost runnerPost = runnerPostQueryRepository.save(
                RunnerPostFixture.create(
                        revieweeRunner,
                        applicantSupporter,
                        new Deadline(LocalDateTime.now().plusHours(100))
                ));
        final SupporterRunnerPost supporterRunnerPost = SupporterRunnerPostFixture.create(runnerPost, applicantSupporter);
        supporterRunnerPostQueryRepository.save(supporterRunnerPost);
        runnerPostQueryRepository.delete(runnerPost);

        // when & then
        assertThatThrownBy(() -> runnerPostCommandService.deleteSupporterRunnerPost(applicantSupporter, runnerPost.getId()))
                .isInstanceOf(RunnerPostBusinessException.class);
    }

    @DisplayName("RunnerPost 의 리뷰 상태가 대기중이 아니면 실패한다.")
    @Test
    void fail_when_runnerPost_reviewStatus_is_not_NOT_STARTED() {
        // given
        final IsReviewed isReviewed = IsReviewed.notReviewed();
        final RunnerPost runnerPost = runnerPostQueryRepository.save(
                RunnerPostFixture.create(
                        revieweeRunner,
                        applicantSupporter,
                        new Deadline(LocalDateTime.now().plusHours(100)),
                        ReviewStatus.IN_PROGRESS,
                        isReviewed
                ));
        final SupporterRunnerPost supporterRunnerPost = SupporterRunnerPostFixture.create(runnerPost, applicantSupporter);
        supporterRunnerPostQueryRepository.save(supporterRunnerPost);


        // when & then
        assertThatThrownBy(() -> runnerPostCommandService.deleteSupporterRunnerPost(applicantSupporter, runnerPost.getId()))
                .isInstanceOf(RunnerPostBusinessException.class);
    }
}
