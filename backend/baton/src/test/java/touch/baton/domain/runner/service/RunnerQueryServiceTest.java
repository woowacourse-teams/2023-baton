package touch.baton.domain.runner.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.query.service.RunnerQueryService;
import touch.baton.domain.technicaltag.command.TechnicalTag;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.TechnicalTagFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class RunnerQueryServiceTest extends ServiceTestConfig {

    private RunnerQueryService runnerQueryService;

    @BeforeEach
    void setUp() {
        runnerQueryService = new RunnerQueryService(runnerQueryRepository);
    }

    @DisplayName("러너를 사용자와 함께 조회한다.")
    @Test
    void readRunnerWithMember() {
        // given
        final Member expectedMember = memberCommandRepository.save(MemberFixture.createEthan());

        final TechnicalTag javaTag = technicalTagQueryRepository.save(TechnicalTagFixture.createJava());
        final TechnicalTag reactTag = technicalTagQueryRepository.save(TechnicalTagFixture.createReact());
        final List<TechnicalTag> technicalTags = List.of(javaTag, reactTag);
        final Runner expectedRunner = runnerQueryRepository.save(RunnerFixture.createRunner(expectedMember, technicalTags));

        // when
        final Runner actualRunner = runnerQueryService.readByRunnerId(expectedRunner.getId());

        // then
        final Member actualMember = actualRunner.getMember();
        assertAll(
                () -> assertThat(actualRunner.getId()).isEqualTo(expectedRunner.getId()),
                () -> assertThat(actualRunner.getIntroduction()).isEqualTo(expectedRunner.getIntroduction()),
                () -> assertThat(actualRunner.getRunnerTechnicalTags().getRunnerTechnicalTags()).containsExactlyElementsOf(expectedRunner.getRunnerTechnicalTags().getRunnerTechnicalTags()),
                () -> assertThat(actualMember.getId()).isEqualTo(expectedMember.getId()),
                () -> assertThat(actualMember.getMemberName()).isEqualTo(expectedMember.getMemberName()),
                () -> assertThat(actualMember.getSocialId()).isEqualTo(expectedMember.getSocialId()),
                () -> assertThat(actualMember.getOauthId()).isEqualTo(expectedMember.getOauthId()),
                () -> assertThat(actualMember.getGithubUrl()).isEqualTo(expectedMember.getGithubUrl()),
                () -> assertThat(actualMember.getCompany()).isEqualTo(expectedMember.getCompany()),
                () -> assertThat(actualMember.getImageUrl()).isEqualTo(expectedMember.getImageUrl())
        );
    }
}
