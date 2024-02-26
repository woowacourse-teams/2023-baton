package touch.baton.common.schedule.deadline.command.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.common.fixture.DeadlineOutboxFixture;
import touch.baton.config.RepositoryTestConfig;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class DeadlineOutboxCommandRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private DeadlineOutboxCommandRepository deadlineOutboxCommandRepository;

    @DisplayName("runnerPostId로 DeadlineOutbox를 삭제할 수 있다.")
    @Test
    void deleteByRunnerPostId() {
        // given
        final long runnerPostId = 1L;
        deadlineOutboxCommandRepository.save(DeadlineOutboxFixture.deadlineOutbox(runnerPostId, Instant.now()));

        // when
        deadlineOutboxCommandRepository.deleteByRunnerPostId(runnerPostId);

        // then
        assertThat(deadlineOutboxCommandRepository.findAll()).isEmpty();
    }
}
