package touch.baton.domain.common.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChattingRoomCountTest {

    @DisplayName("기본 채팅수를 가진 ChattingRoomCount 를 생성할 수 있다.")
    @Test
    void createDefaultChattingRoomCount() {
        // given
        final ChattingRoomCount expected = ChattingRoomCount.zero();

        // when, then
        assertThat(expected.getValue()).isEqualTo(0);
    }
}
