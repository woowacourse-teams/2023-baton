package touch.baton.fixture.domain;

import touch.baton.domain.common.vo.Introduction;
import touch.baton.tobe.domain.member.command.Member;
import touch.baton.tobe.domain.member.command.Runner;
import touch.baton.domain.technicaltag.RunnerTechnicalTag;
import touch.baton.domain.technicaltag.RunnerTechnicalTags;
import touch.baton.domain.technicaltag.TechnicalTag;

import java.util.ArrayList;
import java.util.List;

import static touch.baton.fixture.vo.IntroductionFixture.introduction;

public abstract class RunnerFixture {

    private RunnerFixture() {
    }

    public static Runner createRunner(final Member member) {
        return createRunner(introduction("안녕하세요."), member, new RunnerTechnicalTags(new ArrayList<>()));
    }

    public static Runner createRunner(final Member member, final List<TechnicalTag> technicalTags) {
        final Runner runner = createRunner(introduction("안녕하세요."), member);

        final List<RunnerTechnicalTag> runnerTechnicalTags = technicalTags.stream()
                .map(technicalTag -> RunnerTechnicalTag.builder()
                        .technicalTag(technicalTag)
                        .runner(runner)
                        .build())
                .toList();

        runner.addAllRunnerTechnicalTags(runnerTechnicalTags);
        return runner;
    }

    public static Runner createRunner(final Introduction introduction, final Member member) {
        return Runner.builder()
                .member(member)
                .runnerTechnicalTags(new RunnerTechnicalTags(new ArrayList<>()))
                .build();
    }

    public static Runner createRunner(final Introduction introduction,
                                      final Member member,
                                      final RunnerTechnicalTags runnerTechnicalTags
    ) {
        return Runner.builder()
                .member(member)
                .runnerTechnicalTags(runnerTechnicalTags)
                .build();
    }
}
