package touch.baton.domain.runnerpost.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.exception.RunnerPostBusinessException;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.SupporterRunnerPost;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.SupporterRunnerPostFixture;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RunnerPostUpdateApplicantCancelationServiceTest extends ServiceTestConfig {

    private RunnerPostService runnerPostService;

    private Supporter applicantSupporter;
    private Runner revieweeRunner;

    @BeforeEach
    void setUp() {
        runnerPostService = new RunnerPostService(runnerPostRepository, runnerPostTagRepository, tagRepository, supporterRepository, supporterRunnerPostRepository);

        final Member applicantMember = memberRepository.save(MemberFixture.createDitoo());
        applicantSupporter = supporterRepository.save(SupporterFixture.create(applicantMember));

        final Member revieweeMember = memberRepository.save(MemberFixture.createJudy());
        revieweeRunner = runnerRepository.save(RunnerFixture.createRunner(revieweeMember));
    }

    @DisplayName("성공한다.")
    @Test
    void success() {
        // given
        final RunnerPost runnerPost = runnerPostRepository.save(
                RunnerPostFixture.create(
                        revieweeRunner,
                        applicantSupporter,
                        new Deadline(LocalDateTime.now().plusHours(100))
                ));
        final SupporterRunnerPost supporterRunnerPost = SupporterRunnerPostFixture.create(applicantSupporter, runnerPost);
        supporterRunnerPostRepository.save(supporterRunnerPost);

        // when
        runnerPostService.deleteSupporterRunnerPost(applicantSupporter, runnerPost.getId());

        // then
        assertThat(supporterRunnerPostRepository.findById(supporterRunnerPost.getId())).isNotPresent();
    }

    @DisplayName("RunnerPost 가 존재하지 않으면 실패한다.")
    @Test
    void fail_when_runnerPost_not_found() {
        // given
        final RunnerPost runnerPost = runnerPostRepository.save(
                RunnerPostFixture.create(
                        revieweeRunner,
                        applicantSupporter,
                        new Deadline(LocalDateTime.now().plusHours(100))
                ));
        final SupporterRunnerPost supporterRunnerPost = SupporterRunnerPostFixture.create(applicantSupporter, runnerPost);
        supporterRunnerPostRepository.save(supporterRunnerPost);
        runnerPostRepository.delete(runnerPost);

        // when & then
        assertThatThrownBy(() -> runnerPostService.deleteSupporterRunnerPost(applicantSupporter, runnerPost.getId()))
                .isInstanceOf(RunnerPostBusinessException.class);
    }

    @DisplayName("RunnerPost 의 리뷰 상태가 대기중이 아니면 실패한다.")
    @Test
    void fail_when_runnerPost_reviewStatus_is_not_NOT_STARTED() {
        // given
        final RunnerPost runnerPost = runnerPostRepository.save(
                RunnerPostFixture.create(
                        revieweeRunner,
                        applicantSupporter,
                        new Deadline(LocalDateTime.now().plusHours(100)),
                        ReviewStatus.IN_PROGRESS
                ));
        final SupporterRunnerPost supporterRunnerPost = SupporterRunnerPostFixture.create(applicantSupporter, runnerPost);
        supporterRunnerPostRepository.save(supporterRunnerPost);


        // when & then
        assertThatThrownBy(() -> runnerPostService.deleteSupporterRunnerPost(applicantSupporter, runnerPost.getId()))
                .isInstanceOf(RunnerPostBusinessException.class);
    }
}
