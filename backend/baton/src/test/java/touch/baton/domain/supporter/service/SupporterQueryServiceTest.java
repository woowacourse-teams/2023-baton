package touch.baton.domain.supporter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.ServiceTestConfig;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.tobe.domain.member.command.Member;
import touch.baton.tobe.domain.member.command.Supporter;
import touch.baton.tobe.domain.member.query.service.SupporterQueryService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


class SupporterQueryServiceTest extends ServiceTestConfig {

    private SupporterQueryService supporterQueryService;

    @BeforeEach
    void setUp() {
        supporterQueryService = new SupporterQueryService(supporterQueryRepository);
    }

    @DisplayName("Supporter 식별자 값으로 Member 패치 조인하여 Supporter 를 조회한다.")
    @Test
    void readBySupporterId() {
        // given
        final Member savedMember = memberCommandRepository.save(MemberFixture.createHyena());
        final Supporter savedSupporter = supporterQueryRepository.save(SupporterFixture.create(savedMember));

        // when
        final Supporter foundSupporter = supporterQueryService.readBySupporterId(savedSupporter.getId());

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
