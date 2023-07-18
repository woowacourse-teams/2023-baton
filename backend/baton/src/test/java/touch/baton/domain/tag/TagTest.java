package touch.baton.domain.tag;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import touch.baton.domain.tag.exception.TagException;
import touch.baton.domain.tag.vo.TagCount;
import touch.baton.domain.tag.vo.TagName;

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
                    .tagCount(new TagCount(0))
                    .build()
            ).doesNotThrowAnyException();
        }

        @DisplayName("tag name 이 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_tagName_is_null() {
            assertThatThrownBy(() -> Tag.builder()
                    .tagName(null)
                    .tagCount(new TagCount(0))
                    .build()
            ).isInstanceOf(TagException.NotNull.class);
        }

        @DisplayName("tag count 가 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_tagCount_is_null() {
            assertThatThrownBy(() -> Tag.builder()
                    .tagName(new TagName("자바"))
                    .tagCount(null)
                    .build()
            ).isInstanceOf(TagException.NotNull.class);
        }
    }
}
