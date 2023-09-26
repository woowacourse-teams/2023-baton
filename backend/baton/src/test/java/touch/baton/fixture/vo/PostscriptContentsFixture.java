package touch.baton.fixture.vo;

import touch.baton.tobe.domain.runnerpost.command.vo.PostscriptContents;

public abstract class PostscriptContentsFixture {

    private PostscriptContentsFixture() {
    }

    public static PostscriptContents postscriptContents(final String value) {
        return new PostscriptContents(value);
    }
}
