package touch.baton.domain.tag.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.common.vo.TagName;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.tag.vo.TagReducedName;
import touch.baton.fixture.domain.TagFixture;
import touch.baton.fixture.vo.TagNameFixture;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class TagRepositoryReadTest extends RepositoryTestConfig {

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

    @DisplayName("이름을 오름차순으로 10개 검색한다.")
    @Test
    void success_readTagsByReducedName() {
        // given
        for (int i = 20; i > 0; i--) {
            final Tag tag = TagFixture.create(TagNameFixture.tagName("java" + i));
            tagRepository.save(tag);
        }

        // when
        final List<Tag> actual = tagRepository.readTagsByReducedName("ja");

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(10);
            softly.assertThat(actual.get(0).getTagName().getValue()).isEqualTo("java1");
            softly.assertThat(actual.get(9).getTagName().getValue()).isEqualTo("java18");
        });

    }

    @DisplayName("이름으로 시작하는 태그만 검색한다")
    @Test
    void fail_readTagsByReducedName() {
        // given
        final Tag tag = TagFixture.create(TagNameFixture.tagName("assertj"));
        tagRepository.save(tag);


        // when
        final List<Tag> actual = tagRepository.readTagsByReducedName("j");

        // then
        assertThat(actual.isEmpty()).isTrue();
    }

    @DisplayName("이름에 해당하는 태그가 없다면 빈 배열을 반환한다.")
    @Test
    void success_readTagsByReducedName_when_no_match_tag() {
        // given
        IntStream.iterate(10, i -> i > 0, i -> i - 1)
                .mapToObj(i -> TagFixture.create(TagNameFixture.tagName("java" + i)))
                .forEach(tag -> tagRepository.save(tag));

        // when
        final List<Tag> actual = tagRepository.readTagsByReducedName("hi");

        // then
        assertThat(actual.isEmpty()).isTrue();
    }
}
