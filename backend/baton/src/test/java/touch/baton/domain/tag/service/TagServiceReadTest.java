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
        IntStream.iterate(20, i -> i > 0, i -> i - 1)
                .mapToObj(i -> TagFixture.create(TagNameFixture.tagName("java" + i)))
                .forEach(tag -> tagRepository.save(tag));

        // when
        final List<Tag> actual = tagService.readTagsByReducedName("j a");

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(10);
            softly.assertThat(actual.get(0).getTagName().getValue()).isEqualTo("java1");
            softly.assertThat(actual.get(9).getTagName().getValue()).isEqualTo("java18");
        });
    }
}
