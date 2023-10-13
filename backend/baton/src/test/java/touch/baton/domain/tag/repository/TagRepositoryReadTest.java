package touch.baton.domain.tag.repository;

import jakarta.persistence.EntityManager;
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

    @Autowired
    private EntityManager em;

    @DisplayName("이름으로 단건 검색한다.")
    @Test
    void findByName() {
        // given
        final Tag javaTag = persistTag("java");
        final Tag uppercaseJavaTag = persistTag("Java");

        em.flush();
        em.close();

        // when
        final Optional<Tag> actual = tagRepository.findByTagName(TagNameFixture.tagName("java"));

        // then
        assertThat(actual).contains(javaTag);
    }

    @DisplayName("이름을 오름차순으로 10개 검색한다.")
    @Test
    void success_readTagsByReducedName() {
        // given
        persistTag("ja va");
        persistTag("j ava1");
        persistTag("ja va2");
        persistTag("jav a3");
        persistTag("java 4");
        persistTag("ja va5");
        persistTag("j ava6");
        persistTag("ja va7");
        persistTag("jav a8");
        persistTag("java 9");
        persistTag("assert ja");

        em.flush();
        em.close();

        // when
        final TagReducedName reducedName = TagReducedName.from("ja");
        final List<Tag> actual = tagRepository.readTagsByReducedName(reducedName);

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(10);
            softly.assertThat(actual.get(0).getTagName().getValue()).isEqualTo("ja va");
            softly.assertThat(actual.get(9).getTagName().getValue()).isEqualTo("java 9");
        });

    }

    @DisplayName("이름으로 시작하는 태그만 검색한다")
    @Test
    void fail_readTagsByReducedName() {
        // given
        final Tag tag = TagFixture.create(TagNameFixture.tagName("assertj"));
        final TagReducedName reducedName = TagReducedName.from("j");
        tagRepository.save(tag);

        // when
        final List<Tag> actual = tagRepository.readTagsByReducedName(reducedName);

        // then
        assertThat(actual.isEmpty()).isTrue();
    }

    @DisplayName("이름에 해당하는 태그가 없다면 빈 배열을 반환한다.")
    @Test
    void success_readTagsByReducedName_when_no_match_tag() {
        // given
        final TagReducedName reducedName = TagReducedName.from("hi");
        persistTag("hellohi");

        em.flush();
        em.close();

        // when
        final List<Tag> actual = tagRepository.readTagsByReducedName(reducedName);

        // then
        assertThat(actual.isEmpty()).isTrue();
    }

    private Tag persistTag(final String tagName) {
        final Tag tag = TagFixture.create(TagNameFixture.tagName(tagName));
        em.persist(tag);

        return tag;
    }
}
