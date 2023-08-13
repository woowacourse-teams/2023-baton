package touch.baton.fixture.domain;

import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.SupporterRunnerPost;
import touch.baton.domain.supporter.vo.Message;

public abstract class SupporterRunnerPostFixture {

    private SupporterRunnerPostFixture() {
    }

    public static SupporterRunnerPost create(final Supporter supporter, final RunnerPost runnerPost) {
        return SupporterRunnerPost.builder()
                .supporter(supporter)
                .runnerPost(runnerPost)
                .message(new Message("안녕하세요"))
                .build();
    }
}
