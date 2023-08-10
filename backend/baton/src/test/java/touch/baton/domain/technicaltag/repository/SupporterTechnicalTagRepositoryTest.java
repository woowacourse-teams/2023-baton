package touch.baton.domain.technicaltag.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.technicaltag.SupporterTechnicalTag;
import touch.baton.domain.technicaltag.TechnicalTag;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.TechnicalTagFixture;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class SupporterTechnicalTagRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private SupporterTechnicalTagRepository supporterTechnicalTagRepository;

    @Autowired
    private EntityManager entityManager;

    private Supporter supporter;
    private TechnicalTag technicalTag;

    @BeforeEach
    void setUp() {
        final Member ditoo = MemberFixture.createDitoo();
        entityManager.persist(ditoo);
        supporter = SupporterFixture.create(ditoo);
        entityManager.persist(supporter);
        technicalTag = TechnicalTagFixture.createJava();
        entityManager.persist(technicalTag);
    }

    @DisplayName("Supporter 와 TechnicalTag 로 SupproterTechnicalTag 를 검색해서 존재하면 검색된다.")
    @Test
    void findBySupporterAndTechnicalTag_ifPresent() {
        // given
        final SupporterTechnicalTag expected = supporterTechnicalTagRepository.save(SupporterTechnicalTag.builder()
                .supporter(supporter)
                .technicalTag(technicalTag)
                .build());
        supporterTechnicalTagRepository.flush();

        // when
        final Optional<SupporterTechnicalTag> actual = supporterTechnicalTagRepository.findBySupporterAndTechnicalTag(supporter, technicalTag);

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).isPresent();
            softly.assertThat(expected).isEqualTo(actual.get());
        });
    }

    @DisplayName("Supporter 와 TechnicalTag 로 SupproterTechnicalTag 를 검색해서 존재하지 않으면 검색되지 않는다.")
    @Test
    void findBySupporterAndTechnicalTag_ifNotPresent() {
        // when
        final Optional<SupporterTechnicalTag> actual = supporterTechnicalTagRepository.findBySupporterAndTechnicalTag(supporter, technicalTag);

        // then
        assertThat(actual).isNotPresent();
    }
}
