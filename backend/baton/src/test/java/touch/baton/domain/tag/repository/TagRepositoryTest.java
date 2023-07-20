package touch.baton.domain.tag.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import touch.baton.config.JpaConfig;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.tag.vo.TagName;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Import(JpaConfig.class)
@DataJpaTest
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @DisplayName("이름으로 Tag 를 조회할 때 Tag 가 있으면 조회된다.")
    @Test
    void findByTagName_exist() {
        // given
        final String tagName = "java";
        final Tag expected = Tag.newInstance(tagName);
        tagRepository.saveAndFlush(expected);

        // when
        final Optional<Tag> actual = tagRepository.findByTagName(new TagName(tagName));

        // then
        assertThat(actual).isPresent();
        assertThat(expected).isEqualTo(actual.get());
    }
}
