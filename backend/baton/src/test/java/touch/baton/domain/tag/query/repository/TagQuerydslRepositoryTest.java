package touch.baton.domain.tag.query.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.common.vo.TagName;
import touch.baton.domain.tag.command.Tag;
import touch.baton.domain.tag.command.vo.TagReducedName;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TagQuerydslRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private TagQuerydslRepository tagQuerydslRepository;

    @DisplayName("축약된 태그 이름으로 태그 목록을 limit 개 축약된 태그 이름 기준 오름차순으로 검색한다.")
    @Test
    void readTagsByTagReducedName() {
        // given
        for (int saveTagCount = 1; saveTagCount <= 3; saveTagCount++) {
            persistTag("java" + saveTagCount);
            persistTag("javascript" + saveTagCount);
            persistTag("assertj" + saveTagCount);
        }
        for (int saveTagCount = 4; saveTagCount <= 6; saveTagCount++) {
            persistTag("j ava" + saveTagCount);
            persistTag("j avascript" + saveTagCount);
            persistTag("a ssertj" + saveTagCount);
        }
        for (int saveTagCount = 7; saveTagCount <= 10; saveTagCount++) {
            persistTag("ja va" + saveTagCount);
            persistTag("ja vascript" + saveTagCount);
            persistTag("as sertj" + saveTagCount);
        }

        em.flush();
        em.close();

        // when
        final List<Tag> actual = tagQuerydslRepository.findByTagReducedName(TagReducedName.from("j"), 10);

        // then
        final List<String> actualSortedTagNames = actual.stream()
                .map(Tag::getTagName)
                .map(TagName::getValue)
                .toList();

        assertThat(actualSortedTagNames).containsExactly(
                "java1", "ja va10", "java2", "java3", "j ava4", "j ava5", "j ava6", "ja va7", "ja va8", "ja va9"
        );
    }

    @DisplayName("입력된 TagReducedName 으로 시작하는 태그만 검색한다")
    @Test
    void success_readTagsByReducedName_when_name_isNotMatched_atAll() {
        // given
        persistTag("assertj");

        em.flush();
        em.close();

        // when
        final TagReducedName tagReducedName = TagReducedName.nullableInstance("j");
        final List<Tag> actual = tagQuerydslRepository.findByTagReducedName(tagReducedName, 10);

        // then
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("입력된 TagReducedName 으로 시작하는 이름을 갖는 태그가 없다면 빈 목록을 반환한다.")
    void success_readTagsByReducedName_when_foundTags_isEmpty() {
        // given
        persistTag("aaaaaaaaa");

        em.flush();
        em.close();

        // when
        final TagReducedName tagReducedName = TagReducedName.nullableInstance("b");
        final List<Tag> actual = tagQuerydslRepository.findByTagReducedName(tagReducedName, 10);

        // then
        assertThat(actual.isEmpty()).isTrue();
    }

    @DisplayName("TagReducedName 이 null 인 경우 빈 목록을 반환한다.")
    @Test
    void success_readTagsByReducedName_when_tagReducedNameIsNull() {
        // given
        final TagReducedName tagReducedName = TagReducedName.nullableInstance(null);

        // when
        final List<Tag> actual = tagQuerydslRepository.findByTagReducedName(tagReducedName, 10);

        // then
        assertThat(actual.isEmpty()).isTrue();
    }

    @DisplayName("TagReducedName 내부 값이 blank 인 경우 빈 목록을 반환한다.")
    @Test
    void success_readTagsByReducedName_when_tagReducedNameIsBlank() {
        // given
        final TagReducedName tagReducedName = TagReducedName.nullableInstance("");

        // when
        final List<Tag> actual = tagQuerydslRepository.findByTagReducedName(tagReducedName, 10);

        // then
        assertThat(actual.isEmpty()).isTrue();
    }
}
