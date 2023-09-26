package touch.baton.fixture.domain;

import touch.baton.domain.common.vo.Title;
import touch.baton.domain.common.vo.WatchedCount;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.vo.CuriousContents;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.ImplementedContents;
import touch.baton.domain.runnerpost.vo.IsReviewed;
import touch.baton.domain.runnerpost.vo.PostscriptContents;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.RunnerPostTags;
import touch.baton.domain.tag.Tag;
import touch.baton.fixture.vo.DeadlineFixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class RunnerPostFixture {

    private RunnerPostFixture() {
    }

    public static RunnerPost create(final Title title,
                                    final ImplementedContents implementedContents,
                                    final CuriousContents curiousContents,
                                    final PostscriptContents postscriptContents,
                                    final PullRequestUrl pullRequestUrl,
                                    final Deadline deadline,
                                    final WatchedCount watchedCount,
                                    final ReviewStatus reviewStatus,
                                    final IsReviewed isReviewed,
                                    final Runner runner,
                                    final Supporter supporter,
                                    final RunnerPostTags runnerPostTags
    ) {
        return RunnerPost.builder()
                .title(title)
                .implementedContents(implementedContents)
                .curiousContents(curiousContents)
                .postscriptContents(postscriptContents)
                .pullRequestUrl(pullRequestUrl)
                .deadline(deadline)
                .watchedCount(watchedCount)
                .reviewStatus(reviewStatus)
                .isReviewed(isReviewed)
                .runner(runner)
                .supporter(supporter)
                .runnerPostTags(runnerPostTags)
                .build();
    }

    public static RunnerPost create(final Runner runner, final Deadline deadline) {
        return RunnerPost.builder()
                .title(new Title("테스트 제목"))
                .implementedContents(new ImplementedContents("테스트 내용"))
                .curiousContents(new CuriousContents("테스트 궁금 점"))
                .postscriptContents(new PostscriptContents("테스트 참고 사항"))
                .pullRequestUrl(new PullRequestUrl("https://테스트"))
                .deadline(deadline)
                .watchedCount(new WatchedCount(0))
                .reviewStatus(ReviewStatus.NOT_STARTED)
                .isReviewed(IsReviewed.notReviewed())
                .runner(runner)
                .supporter(null)
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .build();
    }

    public static RunnerPost create(final Runner runner,
                                    final Deadline deadline,
                                    final ReviewStatus reviewStatus,
                                    final IsReviewed isReviewed
    ) {
        return RunnerPost.builder()
                .title(new Title("테스트 제목"))
                .implementedContents(new ImplementedContents("테스트 내용"))
                .curiousContents(new CuriousContents("테스트 궁금 점"))
                .postscriptContents(new PostscriptContents("테스트 참고 사항"))
                .pullRequestUrl(new PullRequestUrl("https://테스트"))
                .deadline(deadline)
                .watchedCount(new WatchedCount(0))
                .reviewStatus(reviewStatus)
                .isReviewed(isReviewed)
                .runner(runner)
                .supporter(null)
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .build();
    }

    public static RunnerPost create(final Runner runner, final Deadline deadline, final List<Tag> tags) {
        final RunnerPost runnerPost = RunnerPost.builder()
                .title(new Title("테스트 제목"))
                .implementedContents(new ImplementedContents("테스트 내용"))
                .curiousContents(new CuriousContents("테스트 궁금 점"))
                .postscriptContents(new PostscriptContents("테스트 참고 사항"))
                .pullRequestUrl(new PullRequestUrl("https://테스트"))
                .deadline(deadline)
                .watchedCount(new WatchedCount(0))
                .reviewStatus(ReviewStatus.NOT_STARTED)
                .isReviewed(IsReviewed.notReviewed())
                .runner(runner)
                .supporter(null)
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .build();

        final List<RunnerPostTag> runnerPostTags = tags.stream()
                .map(tag -> RunnerPostTagFixture.create(runnerPost, tag))
                .toList();

        runnerPost.addAllRunnerPostTags(runnerPostTags);

        return runnerPost;
    }

    public static RunnerPost create(final Runner runner,
                                    final Deadline deadline,
                                    final List<Tag> tags,
                                    final ReviewStatus reviewStatus
    ) {
        final RunnerPost runnerPost = RunnerPost.builder()
                .title(new Title("테스트 제목"))
                .implementedContents(new ImplementedContents("테스트 내용"))
                .curiousContents(new CuriousContents("테스트 궁금 점"))
                .postscriptContents(new PostscriptContents("테스트 참고 사항"))
                .pullRequestUrl(new PullRequestUrl("https://테스트"))
                .deadline(deadline)
                .watchedCount(new WatchedCount(0))
                .reviewStatus(reviewStatus)
                .isReviewed(IsReviewed.notReviewed())
                .runner(runner)
                .supporter(null)
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .build();

        final List<RunnerPostTag> runnerPostTags = tags.stream()
                .map(tag -> RunnerPostTagFixture.create(runnerPost, tag))
                .toList();

        runnerPost.addAllRunnerPostTags(runnerPostTags);

        return runnerPost;
    }

    public static RunnerPost create(final Runner runner, final Supporter supporter) {
        return RunnerPost.builder()
                .title(new Title("테스트 제목"))
                .implementedContents(new ImplementedContents("테스트 내용"))
                .curiousContents(new CuriousContents("테스트 궁금 점"))
                .postscriptContents(new PostscriptContents("테스트 참고 사항"))
                .pullRequestUrl(new PullRequestUrl("https://테스트"))
                .deadline(DeadlineFixture.deadline(LocalDateTime.now().plusHours(100)))
                .watchedCount(new WatchedCount(0))
                .runner(runner)
                .supporter(supporter)
                .reviewStatus(ReviewStatus.NOT_STARTED)
                .isReviewed(IsReviewed.notReviewed())
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .build();
    }

    public static RunnerPost createWithSupporter(final Runner runner,
                                                 final Supporter supporter,
                                                 final ReviewStatus reviewStatus,
                                                 final IsReviewed isReviewed
    ) {
        return RunnerPost.builder()
                .title(new Title("테스트 제목"))
                .implementedContents(new ImplementedContents("테스트 내용"))
                .curiousContents(new CuriousContents("테스트 궁금 점"))
                .postscriptContents(new PostscriptContents("테스트 참고 사항"))
                .pullRequestUrl(new PullRequestUrl("https://테스트"))
                .deadline(DeadlineFixture.deadline(LocalDateTime.now().plusHours(100)))
                .watchedCount(new WatchedCount(0))
                .runner(runner)
                .supporter(supporter)
                .reviewStatus(reviewStatus)
                .isReviewed(isReviewed)
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .build();
    }

    public static RunnerPost create(final Runner runner, final Supporter supporter, final Deadline deadline) {
        return RunnerPost.builder()
                .title(new Title("테스트 제목"))
                .implementedContents(new ImplementedContents("테스트 내용"))
                .curiousContents(new CuriousContents("테스트 궁금 점"))
                .postscriptContents(new PostscriptContents("테스트 참고 사항"))
                .pullRequestUrl(new PullRequestUrl("https://테스트"))
                .deadline(deadline)
                .watchedCount(new WatchedCount(0))
                .runner(runner)
                .supporter(supporter)
                .reviewStatus(ReviewStatus.NOT_STARTED)
                .isReviewed(IsReviewed.notReviewed())
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .build();
    }

    public static RunnerPost create(final Runner runner,
                                    final Supporter supporter,
                                    final Deadline deadline,
                                    final ReviewStatus reviewStatus,
                                    final IsReviewed isReviewed
    ) {
        return RunnerPost.builder()
                .title(new Title("테스트 제목"))
                .implementedContents(new ImplementedContents("테스트 내용"))
                .curiousContents(new CuriousContents("테스트 궁금 점"))
                .postscriptContents(new PostscriptContents("테스트 참고 사항"))
                .pullRequestUrl(new PullRequestUrl("https://테스트"))
                .deadline(deadline)
                .watchedCount(new WatchedCount(0))
                .runner(runner)
                .supporter(supporter)
                .reviewStatus(reviewStatus)
                .isReviewed(isReviewed)
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .build();
    }
}
