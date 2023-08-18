package touch.baton.fixture.vo;

import touch.baton.domain.runnerpost.vo.PullRequestUrl;

public abstract class PullRequestUrlFixture {

    private PullRequestUrlFixture() {
    }

    public static PullRequestUrl pullRequestUrl(final String value) {
        return new PullRequestUrl(value);
    }
}
