package touch.baton.common.schedule.deadline.query.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerPostFixture;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class RunnerPostDeadlineQueryRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private RunnerPostDeadlineQueryRepository runnerPostDeadlineQueryRepository;

    @DisplayName("RunnerPost를 조회할 때 Supporter를 join해서 조회한다.")
    @Test
    void joinSupporterByRunnerPostId() {
        // given
        final Supporter supporter = persistSupporter(MemberFixture.createEthan());
        final Runner runner = persistRunner(MemberFixture.createHyena());
        final RunnerPost runnerPost = persistRunnerPost(RunnerPostFixture.create(runner, supporter));

        // when
        final Optional<RunnerPost> maybeRunnerPost = runnerPostDeadlineQueryRepository.joinSupporterByRunnerPostId(runnerPost.getId());

        // then
        assertAll(
                () -> assertThat(maybeRunnerPost).isPresent(),
                () -> assertThat(maybeRunnerPost).contains(runnerPost)
        );
    }

    @DisplayName("Pessmistic Lock을 사용해서 RunnerPost를 조회할 때 Supporter를 join해서 조회한다.")
    @Test
    void joinSupporterByRunnerPostIdWithLock() {
        // given
        final Supporter supporter = persistSupporter(MemberFixture.createEthan());
        final Runner runner = persistRunner(MemberFixture.createHyena());
        final RunnerPost runnerPost = persistRunnerPost(RunnerPostFixture.create(runner, supporter));

        // when
        final Optional<RunnerPost> maybeRunnerPost = runnerPostDeadlineQueryRepository.joinSupporterByRunnerPostIdWithLock(runnerPost.getId());

        // then
        assertAll(
                () -> assertThat(maybeRunnerPost).isPresent(),
                () -> assertThat(maybeRunnerPost).contains(runnerPost)
        );
    }
}
