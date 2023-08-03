package touch.baton.domain.technicaltag;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.domain.common.vo.TagName;
import touch.baton.domain.tag.exception.TagDomainException;
import touch.baton.domain.tag.exception.TechnicalTagDomainException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TechnicalTagTest {

    @DisplayName("성공한다.")
    @Test
    void success() {
        assertThatCode(() -> TechnicalTag.builder()
                .tagName(new TagName("자바"))
                .build()
        ).doesNotThrowAnyException();
    }

    @DisplayName("tag name 이 null 이 들어갈 경우 예외가 발생한다.")
    @Test
    void fail_if_tagName_is_null() {
        assertThatThrownBy(() -> TechnicalTag.builder()
                .tagName(null)
                .build()
        ).isInstanceOf(TechnicalTagDomainException.class)
                .hasMessage("TechnicalTag 의 tagName 은 null 일 수 없습니다.");
    }
}
