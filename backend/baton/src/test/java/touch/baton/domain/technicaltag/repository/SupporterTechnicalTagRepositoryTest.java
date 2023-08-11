package touch.baton.domain.technicaltag.repository;

import jakarta.persistence.EntityManager;
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
import touch.baton.fixture.domain.SupporterTechnicalTagFixture;
import touch.baton.fixture.domain.TechnicalTagFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SupporterTechnicalTagRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private SupporterTechnicalTagRepository supporterTechnicalTagRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("batch 로 supporter 의 모든 SupporterTechnicalTag 를 삭제한다.")
    @Test
    void deleteBySupporter() {
        // given
        final Member member = MemberFixture.createDitoo();
        entityManager.persist(member);
        final Supporter supporter = SupporterFixture.create(member);
        entityManager.persist(supporter);
        final TechnicalTag technicalTag1 = TechnicalTagFixture.createReact();
        final TechnicalTag technicalTag2 = TechnicalTagFixture.createSpring();
        final TechnicalTag technicalTag3 = TechnicalTagFixture.createJava();
        entityManager.persist(technicalTag1);
        entityManager.persist(technicalTag2);
        entityManager.persist(technicalTag3);
        final SupporterTechnicalTag supporterTechnicalTag1 = SupporterTechnicalTagFixture.create(supporter, technicalTag1);
        final SupporterTechnicalTag supporterTechnicalTag2 = SupporterTechnicalTagFixture.create(supporter, technicalTag2);
        final SupporterTechnicalTag supporterTechnicalTag3 = SupporterTechnicalTagFixture.create(supporter, technicalTag3);
        final List<SupporterTechnicalTag> savedSupporterTechnicalTags = List.of(supporterTechnicalTag1, supporterTechnicalTag2, supporterTechnicalTag3);
        supporterTechnicalTagRepository.saveAll(savedSupporterTechnicalTags);
        entityManager.flush();

        // when
        final int expected = savedSupporterTechnicalTags.size();
        final int actual = supporterTechnicalTagRepository.deleteBySupporter(supporter);

        // then
        assertThat(expected).isEqualTo(actual);
    }
}
