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
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.SupporterRunnerPostFixture;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;

class RunnerPostCommandServiceDeleteTest extends ServiceTestConfig {

    private RunnerPostCommandService runnerPostCommandService;

    @BeforeEach
    void setUp() {
        runnerPostCommandService = new RunnerPostCommandService(
                runnerPostCommandRepository,
                runnerPostQueryRepository,
                tagCommandRepository,
                supporterCommandRepository,
                supporterRunnerPostCommandRepository,
                publisher
        );
    }

    @DisplayName("RunnerPost 식별자값으로 RunnerPost 을 삭제한다.")
    @Test
    void success_deleteByRunnerPostId() {
        // given
        final Member member = memberCommandRepository.save(MemberFixture.createDitoo());
        final Runner runner = runnerQueryRepository.save(RunnerFixture.createRunner(member));
        final RunnerPost runnerPost = runnerPostQueryRepository.save(RunnerPostFixture.create(runner, deadline(LocalDateTime.now().plusHours(10))));

        // when
        runnerPostCommandService.deleteByRunnerPostId(runnerPost.getId(), runner);

        // then
        assertThat(runnerPostQueryRepository.existsById(runnerPost.getId())).isFalse();
    }

    @DisplayName("RunnerPost 식별자값으로 존재하지 않는 RunnerPost 를 삭제 시도할 경우 예외가 발생한다.")
    @Test
    void fail_deleteByRunnerPostId_if_runnerPost_is_null() {
        final Member member = memberCommandRepository.save(MemberFixture.createDitoo());
        final Runner runner = runnerQueryRepository.save(RunnerFixture.createRunner(member));
        assertThatThrownBy(() -> runnerPostCommandService.deleteByRunnerPostId(0L, runner))
                .isInstanceOf(RunnerPostBusinessException.class);
    }

    @DisplayName("RunnerPost 를 작성하지 않은 Runner 가 삭제 시도할 경우 예외가 발생한다.")
    @Test
    void fail_deleteByRunnerPostId_if_not_owner() {
        // given
        final Member memberRunnerPostOwner = memberCommandRepository.save(MemberFixture.createDitoo());
        final Runner runnerPostOwner = runnerQueryRepository.save(RunnerFixture.createRunner(memberRunnerPostOwner));
        final RunnerPost runnerPost = runnerPostQueryRepository.save(RunnerPostFixture.create(
                runnerPostOwner,
                deadline(LocalDateTime.now().plusHours(10))
        ));
        final Member memberRunnerPostNotOwner = memberCommandRepository.save(MemberFixture.createJudy());
        final Runner runnerPostNotOwner = runnerQueryRepository.save(RunnerFixture.createRunner(memberRunnerPostNotOwner));

        // when, then
        assertThatThrownBy(() -> runnerPostCommandService.deleteByRunnerPostId(runnerPost.getId(), runnerPostNotOwner))
                .isInstanceOf(RunnerPostBusinessException.class);
    }

    @DisplayName("NOT_STARTED 상태가 아닌 RunnerPost 를 삭제 시도할 경우 예외가 발생한다.")
    @Test
    void fail_deleteByRunnerPostId_if_reviewStatus_is_not_NOT_STARTED() {
        // given
        final Member memberRunner = memberCommandRepository.save(MemberFixture.createDitoo());
        final Runner runner = runnerQueryRepository.save(RunnerFixture.createRunner(memberRunner));
        final RunnerPost runnerPost = runnerPostQueryRepository.save(RunnerPostFixture.create(
                runner,
                deadline(LocalDateTime.now().plusHours(10))
        ));
        final Member memberSupporter = memberCommandRepository.save(MemberFixture.createEthan());
        final Supporter supporter = supporterQueryRepository.save(SupporterFixture.create(memberSupporter));
        supporterRunnerPostQueryRepository.save(SupporterRunnerPostFixture.create(runnerPost, supporter));
        runnerPost.assignSupporter(supporter);

        // when, then
        assertThatThrownBy(() -> runnerPostCommandService.deleteByRunnerPostId(runnerPost.getId(), runner))
                .isInstanceOf(RunnerPostBusinessException.class);
    }

    @DisplayName("지원한 서포터가 있는 경우에 RunnerPost 를 삭제 시도할 경우 예외가 발생한다.")
    @Test
    void fail_deleteByRunnerPostId_if_applicant_is_exist() {
        // given
        final Member member = memberCommandRepository.save(MemberFixture.createDitoo());
        final Runner runner = runnerQueryRepository.save(RunnerFixture.createRunner(member));
        final RunnerPost runnerPost = runnerPostQueryRepository.save(RunnerPostFixture.create(
                runner,
                deadline(LocalDateTime.now().plusHours(10))
        ));
        final Member memberSupporter = memberCommandRepository.save(MemberFixture.createEthan());
        final Supporter supporter = supporterQueryRepository.save(SupporterFixture.create(memberSupporter));
        supporterRunnerPostQueryRepository.save(SupporterRunnerPostFixture.create(runnerPost, supporter));

        // when, then
        assertThatThrownBy(() -> runnerPostCommandService.deleteByRunnerPostId(runnerPost.getId(), runner))
                .isInstanceOf(RunnerPostBusinessException.class);
    }
}
