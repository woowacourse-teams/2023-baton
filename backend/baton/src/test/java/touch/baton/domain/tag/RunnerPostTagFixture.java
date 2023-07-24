package touch.baton.domain.tag;

import touch.baton.domain.runnerpost.RunnerPost;

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
