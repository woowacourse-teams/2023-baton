package touch.baton.domain.supporter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.command.service.SupporterCommandService;
import touch.baton.domain.member.command.service.dto.SupporterUpdateRequest;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.SupporterFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;


class SupporterCommandServiceTest extends ServiceTestConfig {

    private SupporterCommandService supporterCommandService;

    @BeforeEach
    void setUp() {
        supporterCommandService = new SupporterCommandService(technicalTagQueryRepository, supporterTechnicalTagCommandRepository);
    }

    @DisplayName("Supporter 정보를 수정한다.")
    @Test
    void updateSupporter() {
        // given
        final Member savedMember = memberCommandRepository.save(MemberFixture.createDitoo());
        final Supporter savedSupporter = supporterQueryRepository.save(SupporterFixture.create(savedMember));
        final SupporterUpdateRequest request = new SupporterUpdateRequest("디투랜드", "두나무", "소개글입니다.", List.of("golang", "rust"));

        // when, then
        assertThatCode(() -> supporterCommandService.updateSupporter(savedSupporter, request))
                .doesNotThrowAnyException();
    }
}
