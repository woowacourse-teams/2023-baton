package touch.baton.domain.tag.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.common.vo.TagName;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.tag.vo.TagReducedName;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TagRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private TagRepository tagRepository;

    @DisplayName("이름으로 단건 검색한다.")
    @Test
    void findByName() {
        // given
        final String newTagName = "Java";
        final Tag newTag = Tag.builder()
                .tagName(new TagName(newTagName))
                .tagReducedName(TagReducedName.from(newTagName))
                .build();
        final Tag expected = tagRepository.save(newTag);

        // when
        final Optional<Tag> actual = tagRepository.findByTagName(new TagName(newTagName));

        // then
        assertThat(actual).contains(expected);
    }
}
