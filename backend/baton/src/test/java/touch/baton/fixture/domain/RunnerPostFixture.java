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

import java.time.LocalDateTime;
import java.util.ArrayList;

import static touch.baton.fixture.vo.ChattingCountFixture.*;
import static touch.baton.fixture.vo.ContentsFixture.*;
import static touch.baton.fixture.vo.DeadlineFixture.*;
import static touch.baton.fixture.vo.PullRequestUrlFixture.*;
import static touch.baton.fixture.vo.TitleFixture.*;
import static touch.baton.fixture.vo.WatchedCountFixture.*;

public abstract class RunnerPostFixture {

    private RunnerPostFixture() {
    }

    public static RunnerPost create(final String title,
                                    final String contents,
                                    final String pullRequestUrl,
                                    final LocalDateTime deadline,
                                    final int watchedCount,
                                    final int chattingCount,
                                    final ReviewStatus reviewStatus,
                                    final Runner runner,
                                    final Supporter supporter,
                                    final RunnerPostTags runnerPostTags
    ) {
        return RunnerPost.builder()
                .title(title("테스트 제목"))
                .contents(contents("테스트 내용"))
                .pullRequestUrl(pullRequestUrl("https://테스트"))
                .deadline(deadline(deadline))
                .watchedCount(watchedCount(0))
                .chattingCount(chattingCount(0))
                .runner(runner)
                .supporter(null)
                .runnerPostTags(RunnerPostTagsFixture.runnerPostTags(new ArrayList<>()))
                .build();
    }

    public static RunnerPost create(final Runner runner, final LocalDateTime deadline) {
        return RunnerPost.builder()
                .title(new Title("테스트 제목"))
                .contents(new Contents("테스트 내용"))
                .pullRequestUrl(new PullRequestUrl("https://테스트"))
                .deadline(new Deadline(deadline))
                .watchedCount(new WatchedCount(0))
                .chattingCount(new ChattingCount(0))
                .reviewStatus(ReviewStatus.NOT_STARTED)
                .runner(runner)
                .supporter(null)
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .build();
    }

    public static RunnerPost create(final Runner runner, final RunnerPostTags runnerPostTags, final LocalDateTime deadline) {
        return RunnerPost.builder()
                .title(new Title("테스트 제목"))
                .contents(new Contents("테스트 내용"))
                .pullRequestUrl(new PullRequestUrl("https://테스트"))
                .deadline(new Deadline(deadline))
                .watchedCount(new WatchedCount(0))
                .chattingCount(new ChattingCount(0))
                .runner(runner)
                .supporter(null)
                .runnerPostTags(runnerPostTags)
                .build();
    }

    public static RunnerPost create(final Runner runner, final Supporter supporter, final LocalDateTime deadline) {
        return RunnerPost.builder()
                .title(new Title("테스트 제목"))
                .contents(new Contents("테스트 내용"))
                .pullRequestUrl(new PullRequestUrl("https://테스트"))
                .deadline(new Deadline(deadline))
                .watchedCount(new WatchedCount(0))
                .chattingCount(new ChattingCount(0))
                .runner(runner)
                .supporter(supporter)
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .build();
    }

    public static RunnerPost create(final Runner runner, final Supporter supporter, final RunnerPostTags runnerPostTags, final LocalDateTime deadline) {
        return RunnerPost.builder()
                .title(new Title("테스트 제목"))
                .contents(new Contents("테스트 내용"))
                .pullRequestUrl(new PullRequestUrl("https://테스트"))
                .deadline(new Deadline(deadline))
                .watchedCount(new WatchedCount(0))
                .chattingCount(new ChattingCount(0))
                .runner(runner)
                .supporter(supporter)
                .runnerPostTags(runnerPostTags)
                .build();
    }
}
