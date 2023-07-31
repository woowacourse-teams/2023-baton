package touch.baton.domain.tag;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import touch.baton.domain.common.vo.TagName;
import touch.baton.domain.tag.exception.OldTagException;
import touch.baton.domain.tag.vo.TagCount;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

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
            ).isInstanceOf(OldTagException.NotNull.class);
        }

        @DisplayName("tag count 가 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_tagCount_is_null() {
            assertThatThrownBy(() -> Tag.builder()
                    .tagName(new TagName("자바"))
                    .tagCount(null)
                    .build()
            ).isInstanceOf(OldTagException.NotNull.class);
        }
    }

    @DisplayName("기본 count 를 가진 tag 를 생성할 수 있다.")
    @Test
    void createDefaultTag() {
        // given
        final String tagName = "Java";
        final Tag tag = Tag.newInstance(tagName);

        // when, then
        assertAll(
                () -> assertThat(tag.getTagName()).isEqualTo(new TagName(tagName)),
                () -> assertThat(tag.getTagCount()).isEqualTo(new TagCount(1))
        );
    }

    @DisplayName("Tag 의 count는 1개씩 증가한다.")
    @Test
    void increaseCount() {
        // given
        final Tag tag = Tag.newInstance("Java");

        // when
        tag.increaseCount();

        // then
        assertAll(
                () -> assertThat(tag.getTagName()).isEqualTo(new TagName("Java")),
                () -> assertThat(tag.getTagCount()).isEqualTo(new TagCount(2))
        );

    }
}
