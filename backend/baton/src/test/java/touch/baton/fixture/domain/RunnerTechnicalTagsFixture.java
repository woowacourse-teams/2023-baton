package touch.baton.fixture.domain;

import touch.baton.domain.member.command.Runner;
import touch.baton.domain.technicaltag.command.RunnerTechnicalTag;
import touch.baton.domain.technicaltag.command.RunnerTechnicalTags;
import touch.baton.domain.technicaltag.command.TechnicalTag;

import java.util.List;

public abstract class RunnerTechnicalTagsFixture {

    private RunnerTechnicalTagsFixture() {
    }

    public static RunnerTechnicalTags create(final List<RunnerTechnicalTag> runnerTechnicalTags) {
        return new RunnerTechnicalTags(runnerTechnicalTags);
    }

    public static RunnerTechnicalTags create(final Runner runner, final List<TechnicalTag> technicalTags) {
        final List<RunnerTechnicalTag> runnerTechnicalTags = technicalTags.stream()
                .map(technicalTag -> RunnerTechnicalTag.builder()
                        .technicalTag(technicalTag)
                        .runner(runner)
                        .build())
                .toList();

        return new RunnerTechnicalTags(runnerTechnicalTags);
    }
}
