package touch.baton.domain.tag.query.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.tag.command.Tag;
import touch.baton.domain.tag.command.vo.TagReducedName;
import touch.baton.fixture.domain.TagFixture;
import touch.baton.fixture.vo.TagNameFixture;

import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class TagQueryServiceTest extends ServiceTestConfig {

    private TagQueryService tagQueryService;

    @BeforeEach
    void setUp() {
        tagQueryService = new TagQueryService(tagQuerydslRepository);
    }

    @DisplayName("Tag의 이름으로 Tag를 오름차순으로 10개 조회한다.")
    @Test
    void success_readTagsByReducedName() {
        // given
        tagCommandRepository.save(TagFixture.create(TagNameFixture.tagName("ja va")));
        tagCommandRepository.save(TagFixture.create(TagNameFixture.tagName("jav a1")));
        tagCommandRepository.save(TagFixture.create(TagNameFixture.tagName("j ava2")));
        tagCommandRepository.save(TagFixture.create(TagNameFixture.tagName("ja va3")));
        tagCommandRepository.save(TagFixture.create(TagNameFixture.tagName("jav a4")));
        tagCommandRepository.save(TagFixture.create(TagNameFixture.tagName("java 5")));
        tagCommandRepository.save(TagFixture.create(TagNameFixture.tagName("j ava6")));
        tagCommandRepository.save(TagFixture.create(TagNameFixture.tagName("ja va7")));
        tagCommandRepository.save(TagFixture.create(TagNameFixture.tagName("jav a8")));
        tagCommandRepository.save(TagFixture.create(TagNameFixture.tagName("java 9")));
        tagCommandRepository.save(TagFixture.create(TagNameFixture.tagName("ju ja")));

        // when
        final TagReducedName tagReducedName = TagReducedName.nullableInstance("j a");
        final List<Tag> actual = tagQueryService.readTagsByReducedName(tagReducedName, 10);

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(10);
            softly.assertThat(actual.get(0).getTagName().getValue()).isEqualTo("ja va");
            softly.assertThat(actual.get(9).getTagName().getValue()).isEqualTo("java 9");
        });
    }
}
