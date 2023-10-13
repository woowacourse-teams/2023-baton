package touch.baton.domain.runner.service;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.service.dto.RunnerUpdateRequest;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

class RunnerServiceUpdateTest extends ServiceTestConfig {

    private RunnerService runnerService;

    @BeforeEach
    void setUp() {
        runnerService = new RunnerService(runnerRepository, runnerTechnicalTagRepository, technicalTagRepository);
    }

    @DisplayName("Runner 의 프로필을 수정한다.")
    @Test
    void updateRunnerProfile() {
        // given
        final Member memberJudy = memberRepository.save(MemberFixture.createJudy());
        final Runner runnerJudy = runnerRepository.save(RunnerFixture.createRunner(memberJudy));
        final RunnerUpdateRequest runnerUpdateRequest = new RunnerUpdateRequest("변경된 이름", "변경된 회사", "변경된 자기소개", List.of("changedTag1", "changedTag2"));

        // when, then
        assertThatCode(() -> runnerService.updateRunner(runnerJudy, runnerUpdateRequest))
                .doesNotThrowAnyException();
    }

    @DisplayName("수정된 Runner 의 프로필을 조회하고 검증한다.")
    @Test
    void readUpdatedRunnerProfile() {
        // given
        final Member memberJudy = memberRepository.save(MemberFixture.createJudy());
        final Runner runnerJudy = runnerRepository.save(RunnerFixture.createRunner(memberJudy));
        final RunnerUpdateRequest runnerUpdateRequest = new RunnerUpdateRequest("변경된 이름", "변경된 회사", "변경된 자기소개", List.of("changedTag1", "changedTag2"));

        runnerService.updateRunner(runnerJudy, runnerUpdateRequest);
        final Runner foundRunnerJudy = runnerRepository.findById(runnerJudy.getId()).get();

        // when, then
        assertAll(
                () -> assertThat(foundRunnerJudy.getMember().getMemberName().getValue()).isEqualTo(runnerUpdateRequest.name()),
                () -> assertThat(foundRunnerJudy.getMember().getCompany().getValue()).isEqualTo(runnerUpdateRequest.company()),
                () -> assertThat(foundRunnerJudy.getIntroduction().getValue()).isEqualTo(runnerUpdateRequest.introduction()),
                () -> assertThat(getRunnerTechnicalTags(foundRunnerJudy)).isEqualTo(runnerUpdateRequest.technicalTags())
        );
    }

    @NotNull
    private List<String> getRunnerTechnicalTags(final Runner foundRunnerJudy) {
        return foundRunnerJudy.getRunnerTechnicalTags().getRunnerTechnicalTags().stream()
                .map(runnerTechnicalTag -> runnerTechnicalTag.getTechnicalTag().getTagName().getValue())
                .toList();
    }
}
