package touch.baton.domain.tag.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.tag.Tag;
import touch.baton.fixture.domain.TagFixture;
import touch.baton.fixture.vo.TagNameFixture;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class TagServiceReadTest extends ServiceTestConfig {

    private TagService tagService;

    @BeforeEach
    void setUp() {
        tagService = new TagService(tagRepository);
    }

    @DisplayName("Tag의 이름으로 Tag를 오름차순으로 10개 조회한다.")
    @Test
    void success_readTagsByReducedName() {
        // given
        tagRepository.save(TagFixture.create(TagNameFixture.tagName("ja va")));
        tagRepository.save(TagFixture.create(TagNameFixture.tagName("jav a1")));
        tagRepository.save(TagFixture.create(TagNameFixture.tagName("j ava2")));
        tagRepository.save(TagFixture.create(TagNameFixture.tagName("ja va3")));
        tagRepository.save(TagFixture.create(TagNameFixture.tagName("jav a4")));
        tagRepository.save(TagFixture.create(TagNameFixture.tagName("java 5")));
        tagRepository.save(TagFixture.create(TagNameFixture.tagName("j ava6")));
        tagRepository.save(TagFixture.create(TagNameFixture.tagName("ja va7")));
        tagRepository.save(TagFixture.create(TagNameFixture.tagName("jav a8")));
        tagRepository.save(TagFixture.create(TagNameFixture.tagName("java 9")));
        tagRepository.save(TagFixture.create(TagNameFixture.tagName("ju ja")));

        // when
        final List<Tag> actual = tagService.readTagsByReducedName("j a");

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(10);
            softly.assertThat(actual.get(0).getTagName().getValue()).isEqualTo("ja va");
            softly.assertThat(actual.get(9).getTagName().getValue()).isEqualTo("java 9");
        });
    }
}
