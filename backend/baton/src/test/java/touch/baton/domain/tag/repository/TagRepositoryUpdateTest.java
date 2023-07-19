package touch.baton.domain.tag.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.tag.vo.TagCount;
import touch.baton.domain.tag.vo.TagName;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class TagRepositoryUpdateTest extends RepositoryTestConfig {

    @Autowired
    private TagRepository tagRepository;

    @DisplayName("Tag 식별자값 목록으로 TagCount 수를 -1 씩 줄인다.")
    @Test
    void decreaseTagCountByTagIds() {
        // given
        final Tag tag = Tag.builder()
                .tagName(new TagName("자바"))
                .tagCount(new TagCount(100))
                .build();

        final Tag save = tagRepository.save(tag);

        // when
        tagRepository.decreaseTagCountByTagIds(List.of(tag.getId()));
        final Optional<Tag> maybeTag = tagRepository.findById(tag.getId());

        // then
        assertAll(
                () -> assertThat(maybeTag).isPresent(),
                () -> assertThat(maybeTag.get())
                        .usingRecursiveComparison()
                        .ignoringExpectedNullFields()
                        .isEqualTo(Tag.builder()
                                .tagName(new TagName("자바"))
                                .tagCount(new TagCount(99))
                                .build())
        );
    }
}
