package touch.baton.common.schedule.deadline.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import touch.baton.common.schedule.deadline.command.exception.DeadlineOutboxException;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeadlineOutboxTest {

    @DisplayName("생성 테스트")
    @Nested
    class Create {

        @DisplayName("성공한다")
        @Test
        void success() {
            assertThatCode(() -> DeadlineOutbox.builder()
                    .runnerPostId(1L)
                    .instantToRun(Instant.now())
                    .build()
            ).doesNotThrowAnyException();
        }

        @DisplayName("runnerPostId가 null이면 실패한다")
        @Test
        void fail_runnerPostId_is_null() {
            assertThatThrownBy(() -> DeadlineOutbox.builder()
                    .runnerPostId(null)
                    .instantToRun(Instant.now())
                    .build()
            ).isInstanceOf(DeadlineOutboxException.class);
        }

        @DisplayName("instantToRun이 null이면 실패한다")
        @Test
        void fail_instantToRun_is_null() {
            assertThatThrownBy(() -> DeadlineOutbox.builder()
                    .runnerPostId(1L)
                    .instantToRun(null)
                    .build()
            ).isInstanceOf(DeadlineOutboxException.class);
        }
    }
}
