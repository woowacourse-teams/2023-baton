package touch.baton.domain.runnerpost.command.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.event.RunnerPostApplySupporterEvent;
import touch.baton.domain.runnerpost.command.event.RunnerPostAssignSupporterEvent;
import touch.baton.domain.runnerpost.command.event.RunnerPostReviewStatusDoneEvent;
import touch.baton.domain.runnerpost.command.service.dto.RunnerPostApplicantCreateRequest;
import touch.baton.domain.runnerpost.command.service.dto.RunnerPostUpdateRequest;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;

@RecordApplicationEvents
class RunnerPostCommandServiceEventTest extends ServiceTestConfig {

    @Autowired
    private ApplicationEvents applicationEvents;

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
    }

    @DisplayName("서포터가 러너 게시글에 리뷰를 지원하면 이벤트가 발행된다.")
    @Test
    void success_supporter_apply_runnerPost() {
        // given
        final Member savedMemberDitoo = memberCommandRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitto = runnerQueryRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final Member savedMemberHyena = memberCommandRepository.save(MemberFixture.createHyena());
        final Supporter savedSupporterHyena = supporterCommandRepository.save(SupporterFixture.create(savedMemberHyena));

        final RunnerPost savedRunnerPost = runnerPostCommandRepository.save(RunnerPostFixture.create(savedRunnerDitto, deadline(now().plusHours(100))));

        // when
        final RunnerPostApplicantCreateRequest request = new RunnerPostApplicantCreateRequest("안녕하세요. 서포터 헤나입니다.");
        runnerPostCommandService.createRunnerPostApplicant(savedSupporterHyena, request, savedRunnerPost.getId());

        // then
        final long eventPublishedCount = applicationEvents.stream(RunnerPostApplySupporterEvent.class).count();

        assertThat(eventPublishedCount).isOne();
    }

    @DisplayName("러너는 자신의 러너 게시글의 지원자 중 한 명을 서포터로서 확정하면 이벤트가 발행된다.")
    @Test
    void success_runner_assign_applicant_supporter() {
        // given
        final Member savedMemberDitoo = memberCommandRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitto = runnerQueryRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final Member savedMemberHyena = memberCommandRepository.save(MemberFixture.createHyena());
        final Supporter savedSupporterHyena = supporterCommandRepository.save(SupporterFixture.create(savedMemberHyena));

        final RunnerPost savedRunnerPost = runnerPostCommandRepository.save(RunnerPostFixture.create(savedRunnerDitto, deadline(now().plusHours(100))));

        final RunnerPostApplicantCreateRequest runnerPostApplicantCreateRequest = new RunnerPostApplicantCreateRequest("안녕하세요. 서포터 헤나입니다.");
        runnerPostCommandService.createRunnerPostApplicant(savedSupporterHyena, runnerPostApplicantCreateRequest, savedRunnerPost.getId());

        // when
        final RunnerPostUpdateRequest.SelectSupporter runnerPostAssignSupporterRequest = new RunnerPostUpdateRequest.SelectSupporter(savedSupporterHyena.getId());
        runnerPostCommandService.updateRunnerPostAppliedSupporter(savedRunnerDitto, savedRunnerPost.getId(), runnerPostAssignSupporterRequest);

        // then
        final long eventPublishedCount = applicationEvents.stream(RunnerPostAssignSupporterEvent.class).count();

        assertThat(eventPublishedCount).isOne();
    }

    @DisplayName("서포터가 러너 게시글의 상태를 리뷰 완료로 변경할 경우 이벤트가 발행된다.")
    @Test
    void success_supporter_update_runnerPost_reviewStatus_done() {
        final Member savedMemberDitoo = memberCommandRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitto = runnerQueryRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final Member savedMemberHyena = memberCommandRepository.save(MemberFixture.createHyena());
        final Supporter savedSupporterHyena = supporterCommandRepository.save(SupporterFixture.create(savedMemberHyena));

        final RunnerPost savedRunnerPost = runnerPostCommandRepository.save(RunnerPostFixture.create(savedRunnerDitto, deadline(now().plusHours(100))));

        final RunnerPostApplicantCreateRequest runnerPostApplicantCreateRequest = new RunnerPostApplicantCreateRequest("안녕하세요. 서포터 헤나입니다.");
        runnerPostCommandService.createRunnerPostApplicant(savedSupporterHyena, runnerPostApplicantCreateRequest, savedRunnerPost.getId());

        final RunnerPostUpdateRequest.SelectSupporter runnerPostAssignSupporterRequest = new RunnerPostUpdateRequest.SelectSupporter(savedSupporterHyena.getId());
        runnerPostCommandService.updateRunnerPostAppliedSupporter(savedRunnerDitto, savedRunnerPost.getId(), runnerPostAssignSupporterRequest);

        // when
        runnerPostCommandService.updateRunnerPostReviewStatusDone(savedRunnerPost.getId(), savedSupporterHyena);

        // then
        final long eventPublishedCount = applicationEvents.stream(RunnerPostReviewStatusDoneEvent.class).count();

        assertThat(eventPublishedCount).isOne();
    }
}
