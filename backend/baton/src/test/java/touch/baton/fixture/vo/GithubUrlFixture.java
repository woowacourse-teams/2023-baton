package touch.baton.fixture.vo;

import touch.baton.domain.member.command.vo.GithubUrl;

public abstract class GithubUrlFixture {

    private GithubUrlFixture() {
    }

    public static GithubUrl githubUrl(final String value) {
        return new GithubUrl(value);
    }
}
