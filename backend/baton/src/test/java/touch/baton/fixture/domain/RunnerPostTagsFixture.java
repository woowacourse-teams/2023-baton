package touch.baton.fixture.domain;

import touch.baton.tobe.domain.tag.command.RunnerPostTag;
import touch.baton.tobe.domain.tag.command.RunnerPostTags;

import java.util.List;

public abstract class RunnerPostTagsFixture {

    private RunnerPostTagsFixture() {
    }

    public static RunnerPostTags runnerPostTags(final List<RunnerPostTag> runnerPostTags) {
        return new RunnerPostTags(runnerPostTags);
    }
}
