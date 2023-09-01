package touch.baton.domain.tag;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import touch.baton.domain.common.vo.TagName;
import touch.baton.domain.tag.exception.TagDomainException;
import touch.baton.domain.tag.vo.TagReducedName;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TagTest {

    @DisplayName("생성 테스트")
    @Nested
    class Create {

        @DisplayName("성공한다.")
        @Test
        void success() {
            assertThatCode(() -> Tag.builder()
                    .tagName(new TagName("자바"))
                    .tagReducedName(TagReducedName.from("자바"))
                    .build()
            ).doesNotThrowAnyException();
        }

        @DisplayName("tag name 이 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_tagName_is_null() {
            assertThatThrownBy(() -> Tag.builder()
                    .tagName(null)
                    .tagReducedName(TagReducedName.from("hello"))
                    .build()
            ).isInstanceOf(TagDomainException.class);
        }

        @DisplayName("tag reduced name 이 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_tagReducedName_is_null() {
            assertThatThrownBy(() -> Tag.builder()
                    .tagName(new TagName("hello"))
                    .tagReducedName(null)
                    .build()
            ).isInstanceOf(TagDomainException.class);
        }
    }
}
