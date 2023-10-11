package touch.baton.domain.common.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.domain.runnerpost.command.vo.WatchedCount;

import static org.assertj.core.api.Assertions.assertThat;

class WatchedCountTest {

    @DisplayName("기본 조회수를 가진 WatchedCount 를 생성할 수 있다.")
    @Test
    void createDefaultWatchedCount() {
        // given
        final WatchedCount expected = WatchedCount.zero();

        // when, then
        assertThat(expected.getValue()).isEqualTo(0);
    }

    @DisplayName("조회수를 증가시킨다.")
    @Test
    void increase() {
        // given
        final WatchedCount watchedCount = WatchedCount.zero();

        // when
        final WatchedCount increasedWatchedCount = watchedCount.increase();

        // then
        assertThat(increasedWatchedCount).isEqualTo(new WatchedCount(1));
    }
}
