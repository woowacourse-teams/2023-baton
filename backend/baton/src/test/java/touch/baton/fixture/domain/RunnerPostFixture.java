package touch.baton.fixture.domain;

import touch.baton.domain.common.vo.ChattingCount;
import touch.baton.domain.common.vo.Contents;
import touch.baton.domain.common.vo.Title;
import touch.baton.domain.common.vo.WatchedCount;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.tag.RunnerPostTags;

import java.util.ArrayList;

public abstract class RunnerPostFixture {

    private RunnerPostFixture() {
    }

    public static RunnerPost create(final Title title,
                                    final Contents contents,
                                    final PullRequestUrl pullRequestUrl,
                                    final Deadline deadline,
                                    final WatchedCount watchedCount,
                                    final ChattingCount chattingCount,
                                    final ReviewStatus reviewStatus,
                                    final Runner runner,
                                    final Supporter supporter,
                                    final RunnerPostTags runnerPostTags
    ) {
        return RunnerPost.builder()
                .title(title)
                .contents(contents)
                .pullRequestUrl(pullRequestUrl)
                .deadline(deadline)
                .watchedCount(watchedCount)
                .chattingCount(chattingCount)
                .reviewStatus(reviewStatus)
                .runner(runner)
                .supporter(supporter)
                .runnerPostTags(runnerPostTags)
                .build();
    }

    public static RunnerPost create(final Runner runner, final Deadline deadline) {
        return RunnerPost.builder()
                .title(new Title("테스트 제목"))
                .contents(new Contents("테스트 내용"))
                .pullRequestUrl(new PullRequestUrl("https://테스트"))
                .deadline(deadline)
                .watchedCount(new WatchedCount(0))
                .chattingCount(new ChattingCount(0))
                .reviewStatus(ReviewStatus.NOT_STARTED)
                .runner(runner)
                .supporter(null)
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .build();
    }

    public static RunnerPost create(final Runner runner, final RunnerPostTags runnerPostTags, final Deadline deadline) {
        return RunnerPost.builder()
                .title(new Title("테스트 제목"))
                .contents(new Contents("테스트 내용"))
                .pullRequestUrl(new PullRequestUrl("https://테스트"))
                .deadline(deadline)
                .watchedCount(new WatchedCount(0))
                .chattingCount(new ChattingCount(0))
                .runner(runner)
                .supporter(null)
                .runnerPostTags(runnerPostTags)
                .build();
    }

    public static RunnerPost create(final Runner runner, final Supporter supporter, final Deadline deadline) {
        return RunnerPost.builder()
                .title(new Title("테스트 제목"))
                .contents(new Contents("테스트 내용"))
                .pullRequestUrl(new PullRequestUrl("https://테스트"))
                .deadline(deadline)
                .watchedCount(new WatchedCount(0))
                .chattingCount(new ChattingCount(0))
                .runner(runner)
                .supporter(supporter)
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .build();
    }

    public static RunnerPost create(final Runner runner, final Supporter supporter, final RunnerPostTags runnerPostTags, final Deadline deadline) {
        return RunnerPost.builder()
                .title(new Title("테스트 제목"))
                .contents(new Contents("테스트 내용"))
                .pullRequestUrl(new PullRequestUrl("https://테스트"))
                .deadline(deadline)
                .watchedCount(new WatchedCount(0))
                .chattingCount(new ChattingCount(0))
                .runner(runner)
                .supporter(supporter)
                .runnerPostTags(runnerPostTags)
                .build();
    }
}
