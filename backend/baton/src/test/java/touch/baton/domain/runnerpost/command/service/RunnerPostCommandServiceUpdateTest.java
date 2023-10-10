package touch.baton.domain.runnerpost.command.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.exception.RunnerPostBusinessException;
import touch.baton.domain.runnerpost.command.exception.RunnerPostDomainException;
import touch.baton.domain.runnerpost.command.service.dto.RunnerPostUpdateRequest;
import touch.baton.domain.runnerpost.command.vo.IsReviewed;
import touch.baton.domain.runnerpost.command.vo.ReviewStatus;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.SupporterRunnerPostFixture;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static touch.baton.domain.runnerpost.command.vo.ReviewStatus.IN_PROGRESS;
import static touch.baton.domain.runnerpost.command.vo.ReviewStatus.OVERDUE;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;

class RunnerPostCommandServiceUpdateTest extends ServiceTestConfig {

    private static Runner runnerPostOwner;
    private static RunnerPost targetRunnerPost;
    private static Supporter applySupporter;
    private static Runner runner;
    private static Supporter assignedSupporter;

    private RunnerPostCommandService runnerPostCommandService;

    @BeforeEach
    void setUp() {
        runnerPostCommandService = new RunnerPostCommandService(
                runnerPostCommandRepository,
                tagCommandRepository,
                supporterCommandRepository,
                supporterRunnerPostCommandRepository,
                publisher
        );

        final Member ehtanMember = memberCommandRepository.save(MemberFixture.createEthan());
        runnerPostOwner = runnerQueryRepository.save(RunnerFixture.createRunner(ehtanMember));
        targetRunnerPost = runnerPostQueryRepository.save(RunnerPostFixture.create(runnerPostOwner,
                deadline(LocalDateTime.now().plusDays(10))));

        final Member hyenaMember = memberCommandRepository.save(MemberFixture.createHyena());
        applySupporter = supporterQueryRepository.save(SupporterFixture.create(hyenaMember));
        supporterRunnerPostQueryRepository.save(SupporterRunnerPostFixture.create(targetRunnerPost, applySupporter));

        final Member runnerMember = memberCommandRepository.save(MemberFixture.createEthan());
        runner = runnerQueryRepository.save(RunnerFixture.createRunner(runnerMember));

        final Member supporterMember = memberCommandRepository.save(MemberFixture.createDitoo());
        assignedSupporter = supporterQueryRepository.save(SupporterFixture.create(supporterMember));
    }

    @DisplayName("러너는 자신의 글에 제안한 서포터를 서포터로 선택할 수 있다.")
    @Test
    void updateRunnerPostAppliedSupporter() {
        // given
        final RunnerPostUpdateRequest.SelectSupporter request = new RunnerPostUpdateRequest.SelectSupporter(applySupporter.getId());

        // when
        runnerPostCommandService.updateRunnerPostAppliedSupporter(runnerPostOwner, targetRunnerPost.getId(), request);

        // then
        final Optional<RunnerPost> maybeRunnerPost = runnerPostQueryRepository.findById(targetRunnerPost.getId());
        assertThat(maybeRunnerPost).isPresent();

        final RunnerPost actualRunnerPost = maybeRunnerPost.get();
        assertAll(
                () -> assertThat(actualRunnerPost.getSupporter().getId()).isEqualTo(applySupporter.getId()),
                () -> assertThat(actualRunnerPost.getReviewStatus()).isEqualTo(ReviewStatus.IN_PROGRESS)
        );
    }

    @DisplayName("러너는 가입되어 있지 않는 서포터를 선택할 수 없다.")
    @Test
    void fail_updateRunnerPostAppliedSupporter_if_not_join_supporter() {
        // given
        final Long notJoinSupporterId = 1000000L;
        final RunnerPostUpdateRequest.SelectSupporter request = new RunnerPostUpdateRequest.SelectSupporter(notJoinSupporterId);

        // when, then
        assertThatThrownBy(() -> runnerPostCommandService.updateRunnerPostAppliedSupporter(runnerPostOwner, targetRunnerPost.getId(), request))
                .isInstanceOf(RunnerPostBusinessException.class);
    }

    @DisplayName("러너는 자신의 글에 제안한 서포터가 아니면 서포터로 선택할 수 없다.")
    @Test
    void fail_updateRunnerPostAppliedSupporter_if_not_apply_supporter() {
        // given
        final Member ditooMember = memberCommandRepository.save(MemberFixture.createDitoo());
        final Supporter notApplySupporter = supporterQueryRepository.save(SupporterFixture.create(ditooMember));

        final RunnerPostUpdateRequest.SelectSupporter request = new RunnerPostUpdateRequest.SelectSupporter(notApplySupporter.getId());

        // when, then
        assertThatThrownBy(() -> runnerPostCommandService.updateRunnerPostAppliedSupporter(runnerPostOwner, targetRunnerPost.getId(), request))
                .isInstanceOf(RunnerPostBusinessException.class);
    }

    @DisplayName("러너는 작성된 글이 아니면 서포터를 선택할 수 없다.")
    @Test
    void fail_updateRunnerPostAppliedSupporter_if_is_not_written_runnerPost() {
        // given
        final RunnerPostUpdateRequest.SelectSupporter request = new RunnerPostUpdateRequest.SelectSupporter(applySupporter.getId());
        final Long notWrittenRunnerPostId = 1000000L;

        // when, then
        assertThatThrownBy(() -> runnerPostCommandService.updateRunnerPostAppliedSupporter(runnerPostOwner, notWrittenRunnerPostId, request))
                .isInstanceOf(RunnerPostBusinessException.class);
    }

    @DisplayName("러너는 자신의 글이 아니면 서포터를 선택할 수 없다.")
    @Test
    void fail_updateRunnerPostAppliedSupporter_if_is_not_owner_of_runnerPost() {
        // given
        final Member ditooMember = memberCommandRepository.save(MemberFixture.createDitoo());
        final Runner notOwnerRunner = runnerQueryRepository.save(RunnerFixture.createRunner(ditooMember));

        final RunnerPostUpdateRequest.SelectSupporter request = new RunnerPostUpdateRequest.SelectSupporter(applySupporter.getId());

        // when, then
        assertThatThrownBy(() -> runnerPostCommandService.updateRunnerPostAppliedSupporter(notOwnerRunner, targetRunnerPost.getId(), request))
                .isInstanceOf(RunnerPostBusinessException.class);
    }

    @DisplayName("리뷰가 완료되면 서포터는 게시글의 상태를 리뷰 완료로 변경할 수 있다.")
    @Test
    void updateRunnerPostReviewStatusDone() {
        // given
        final IsReviewed isReviewed = IsReviewed.notReviewed();
        final RunnerPost targetRunnerPost = runnerPostQueryRepository.save(RunnerPostFixture.createWithSupporter(runner, assignedSupporter, IN_PROGRESS, isReviewed));

        // when
        runnerPostCommandService.updateRunnerPostReviewStatusDone(targetRunnerPost.getId(), assignedSupporter);

        // then
        final Optional<RunnerPost> maybeRunnerPost = runnerPostQueryRepository.findById(targetRunnerPost.getId());
        assertThat(maybeRunnerPost).isPresent();
        final RunnerPost actualRunnerPost = maybeRunnerPost.get();
        assertThat(actualRunnerPost.getReviewStatus()).isEqualTo(ReviewStatus.DONE);
    }

    @DisplayName("없는 게시글의 상태를 리뷰 완료로 변경할 수 없다.")
    @Test
    void fail_updateRunnerPostReviewStatusDone_if_invalid_runnerPostId() {
        // given
        final IsReviewed isReviewed = IsReviewed.notReviewed();
        runnerPostQueryRepository.save(RunnerPostFixture.createWithSupporter(runner, assignedSupporter, IN_PROGRESS, isReviewed));
        final Long unsavedRunnerPostId = 100000L;

        // when, then
        assertThatThrownBy(() -> runnerPostCommandService.updateRunnerPostReviewStatusDone(unsavedRunnerPostId, assignedSupporter))
                .isInstanceOf(RunnerPostBusinessException.class);
    }

    @DisplayName("서포터가 배정 되지 않은 게시글의 상태를 리뷰 완료로 변경할 수 없다.")
    @Test
    void fail_updateRunnerPostReviewStatusDone_if_supporter_is_null() {
        // given
        final IsReviewed isReviewed = IsReviewed.notReviewed();
        final RunnerPost targetRunnerPost = runnerPostQueryRepository.save(RunnerPostFixture.createWithSupporter(runner, null, IN_PROGRESS, isReviewed));

        // when, then
        assertThatThrownBy(() -> runnerPostCommandService.updateRunnerPostReviewStatusDone(targetRunnerPost.getId(), assignedSupporter))
                .isInstanceOf(RunnerPostBusinessException.class);
    }

    @DisplayName("다른 서포터가 리뷰 중인 게시글의 상태를 리뷰 완료로 변경할 수 없다.")
    @Test
    void fail_updateRunnerPostReviewStatusDone_if_different_supporter_is_assigned() {
        // given
        final IsReviewed isReviewed = IsReviewed.notReviewed();
        final RunnerPost targetRunnerPost = runnerPostQueryRepository.save(RunnerPostFixture.createWithSupporter(runner, assignedSupporter, IN_PROGRESS, isReviewed));
        final Member differentMember = memberCommandRepository.save(MemberFixture.createHyena());
        final Supporter differentSupporter = supporterQueryRepository.save(SupporterFixture.create(differentMember));

        // when, then
        assertThatThrownBy(() -> runnerPostCommandService.updateRunnerPostReviewStatusDone(targetRunnerPost.getId(), differentSupporter))
                .isInstanceOf(RunnerPostBusinessException.class);
    }

    @DisplayName("만료된 리뷰 게시글의 상태를 리뷰 완료로 변경할 수 없다.")
    @Test
    void fail_updateRunnerPostReviewStatusDone_if_reviewStatus_is_overdue() {
        // given
        final IsReviewed isReviewed = IsReviewed.notReviewed();
        final RunnerPost targetRunnerPost = runnerPostQueryRepository.save(RunnerPostFixture.createWithSupporter(runner, assignedSupporter, OVERDUE, isReviewed));

        // when, then
        assertThatThrownBy(() -> runnerPostCommandService.updateRunnerPostReviewStatusDone(targetRunnerPost.getId(), assignedSupporter))
                .isInstanceOf(RunnerPostDomainException.class);
    }
}
