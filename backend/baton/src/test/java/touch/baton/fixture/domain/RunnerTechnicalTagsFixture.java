package touch.baton.fixture.domain;

import touch.baton.tobe.domain.member.command.Runner;
import touch.baton.domain.technicaltag.RunnerTechnicalTag;
import touch.baton.domain.technicaltag.RunnerTechnicalTags;
import touch.baton.domain.technicaltag.TechnicalTag;

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
