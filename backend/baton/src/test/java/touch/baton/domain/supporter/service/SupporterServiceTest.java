package touch.baton.domain.supporter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.supporter.Supporter;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.SupporterFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class SupporterServiceTest extends ServiceTestConfig {

    private SupporterService supporterService;

    @BeforeEach
    void setUp() {
        supporterService = new SupporterService(supporterRepository);
    }

    @DisplayName("Supporter 식별자 값으로 Member 패치 조인하여 Supporte 를 조회한다.")
    @Test
    void readBySupporterId() {
        // given
        final Member savedMember = memberRepository.save(MemberFixture.createHyena());
        final Supporter savedSupporter = supporterRepository.save(SupporterFixture.create(savedMember));

        // when
        final Supporter foundSupporter = supporterService.readBySupporterId(savedSupporter.getId());

        // then
        assertAll(
                () -> assertThat(foundSupporter.getId()).isEqualTo(savedSupporter.getId()),
                () -> assertThat(foundSupporter.getIntroduction()).isEqualTo(savedSupporter.getIntroduction()),
                () -> assertThat(foundSupporter.getReviewCount()).isEqualTo(savedSupporter.getReviewCount()),
                () -> assertThat(foundSupporter.getSupporterTechnicalTags()).isEqualTo(savedSupporter.getSupporterTechnicalTags()),
                () -> assertThat(foundSupporter.getMember()).isEqualTo(savedMember)
        );
    }
}
