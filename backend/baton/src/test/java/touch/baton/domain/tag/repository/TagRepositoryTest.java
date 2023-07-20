package touch.baton.domain.tag.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import touch.baton.config.JpaConfig;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.tag.vo.TagCount;
import touch.baton.domain.tag.vo.TagName;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Import(JpaConfig.class)
@DataJpaTest
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @DisplayName("이름으로 단건 검색한다.")
    @Test
    void findByName() {
        // given
        final String newTagName = "Java";
        final Tag newTag = Tag.builder()
                .tagName(new TagName(newTagName))
                .tagCount(new TagCount(0))
                .build();
        final Tag expected = tagRepository.save(newTag);

        // when
        final Optional<Tag> actual = tagRepository.findByTagName(new TagName(newTagName));

        // then
        assertThat(actual).contains(expected);
    }
}
