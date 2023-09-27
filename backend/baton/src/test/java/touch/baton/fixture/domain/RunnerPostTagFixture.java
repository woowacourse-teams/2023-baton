package touch.baton.fixture.domain;

import touch.baton.tobe.domain.runnerpost.command.RunnerPost;
import touch.baton.tobe.domain.tag.command.RunnerPostTag;
import touch.baton.tobe.domain.tag.command.Tag;

public abstract class RunnerPostTagFixture {

    private RunnerPostTagFixture() {
    }

    public static RunnerPostTag create(final RunnerPost runnerPost, final Tag tag) {
        return RunnerPostTag.builder()
                .runnerPost(runnerPost)
                .tag(tag)
                .build();
    }
}
