package touch.baton.domain.member.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.domain.member.command.vo.MemberName;

import static org.assertj.core.api.Assertions.assertThat;

class MemberNameTest {

    @DisplayName("value 가 null 이면 기본값으로_생성한다")
    @Test
    void fail_if_value_is_null_then_default_value() {
        // given
        final MemberName memberName = new MemberName(null);

        // when, then
        assertThat(memberName.getValue()).isEqualTo("익명의 사용자");
    }
}
