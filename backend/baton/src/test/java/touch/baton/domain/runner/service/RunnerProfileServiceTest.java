package touch.baton.domain.runner.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RunnerProfileServiceTest extends ServiceTestConfig {

    private RunnerProfileService runnerProfileService;

    @BeforeEach
    void setUp() {
        runnerProfileService = new RunnerProfileService(runnerTechnicalTagRepository, technicalTagRepository);
    }

    @DisplayName("Runner 의 프로필을 수정한다.")
    @Test
    void updateRunnerProfile() {
        // given
        final Member memberJudy = memberRepository.save(MemberFixture.createJudy());
        final Runner runnerJudy = runnerRepository.save(RunnerFixture.createRunner(memberJudy));
        final RunnerProfileRequest runnerProfileRequest = new RunnerProfileRequest("변경된 이름", "변경된 회사", "변경된 자기소개", new String[]{"changedTag1", "changedTag2"});

        // when
        runnerProfileService.updateRunnerProfile(runnerJudy, runnerProfileRequest);

        // then
        assertAll(
                () -> assertThat(runnerJudy.getMember().getMemberName().getValue()).isEqualTo("변경된 이름"),
                () -> assertThat(runnerJudy.getMember().getCompany().getValue()).isEqualTo("변경된 회사"),
                () -> assertThat(runnerJudy.getIntroduction().getValue()).isEqualTo("변경된 자기소개"),
                () -> assertThat(runnerJudy.getRunnerTechnicalTags().getRunnerTechnicalTags().get(0).getTechnicalTag().getTagName().getValue()).isEqualTo("changedTag1"),
                () -> assertThat(runnerJudy.getRunnerTechnicalTags().getRunnerTechnicalTags().get(1).getTechnicalTag().getTagName().getValue()).isEqualTo("changedTag2")
        );
    }
}
