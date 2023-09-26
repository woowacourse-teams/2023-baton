package touch.baton.fixture.domain;

import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.Tag;
import touch.baton.tobe.domain.runnerpost.command.RunnerPost;

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
