package touch.baton.fixture.domain;

import touch.baton.tobe.domain.member.command.Supporter;
import touch.baton.tobe.domain.member.command.SupporterRunnerPost;
import touch.baton.tobe.domain.member.command.vo.Message;
import touch.baton.tobe.domain.runnerpost.command.RunnerPost;

public abstract class SupporterRunnerPostFixture {

    private SupporterRunnerPostFixture() {
    }

    public static SupporterRunnerPost create(final RunnerPost runnerPost, final Supporter supporter) {
        return SupporterRunnerPost.builder()
                .runnerPost(runnerPost)
                .supporter(supporter)
                .message(new Message("안녕하세요. 테스트용 서포터입니다."))
                .build();
    }
}
