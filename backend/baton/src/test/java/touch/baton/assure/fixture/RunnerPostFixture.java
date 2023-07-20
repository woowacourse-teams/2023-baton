package touch.baton.assure.fixture;

import touch.baton.domain.common.vo.ChattingRoomCount;
import touch.baton.domain.common.vo.Contents;
import touch.baton.domain.common.vo.Title;
import touch.baton.domain.common.vo.WatchedCount;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.tag.RunnerPostTags;

import java.time.LocalDateTime;

public abstract class RunnerPostFixture {

    private RunnerPostFixture() {
    }

    public static RunnerPost from(final Runner runner,
                                  final Supporter supporter,
                                  final String title,
                                  final String contents,
                                  final String pullRequestUrl,
                                  final LocalDateTime deadline,
                                  final Integer watchedCount,
                                  final Integer chattingRoomCount,
                                  final RunnerPostTags runnerPostTags
    ) {
        return RunnerPost.builder()
                .title(new Title(title))
                .contents(new Contents(contents))
                .pullRequestUrl(new PullRequestUrl(pullRequestUrl))
                .deadline(new Deadline(deadline))
                .watchedCount(new WatchedCount(watchedCount))
                .chattingRoomCount(new ChattingRoomCount(chattingRoomCount))
                .runner(runner)
                .supporter(supporter)
                .runnerPostTags(runnerPostTags)
                .build();
    }
}
