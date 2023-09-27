package touch.baton.domain.technicaltag.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.common.vo.TagName;
import touch.baton.fixture.domain.TechnicalTagFixture;
import touch.baton.fixture.vo.TagNameFixture;
import touch.baton.tobe.domain.technicaltag.command.TechnicalTag;
import touch.baton.tobe.domain.technicaltag.query.repository.TechnicalTagQueryRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class TechnicalTagQueryRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private TechnicalTagQueryRepository technicalTagQueryRepository;

    @DisplayName("TagName 으로 TechnicalTag 를 검색할 때 TechnicalTag 가 존재하면 검색된다.")
    @Test
    void findByName_ifPresent() {
        // given
        final TagName tagName = TagNameFixture.tagName("java");
        final TechnicalTag expected = technicalTagQueryRepository.save(TechnicalTagFixture.create(tagName));
        technicalTagQueryRepository.flush();

        // when
        final Optional<TechnicalTag> actual = technicalTagQueryRepository.findByTagName(tagName);

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).isPresent();
            softly.assertThat(expected).isEqualTo(actual.get());
        });
    }

    @DisplayName("TagName 으로 TechnicalTag 를 검색할 때 TechnicalTag 가 존재하지 않으면 검색되지 않는다.")
    @Test
    void findByName_ifNotPresent() {
        // given
        final TagName tagName = new TagName("java");

        // when
        final Optional<TechnicalTag> actual = technicalTagQueryRepository.findByTagName(tagName);

        // then
        assertThat(actual).isNotPresent();
    }
}
