package touch.baton.domain.runnerpost;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import touch.baton.domain.common.BaseEntity;
import touch.baton.domain.common.vo.ChattingCount;
import touch.baton.domain.common.vo.Contents;
import touch.baton.domain.common.vo.Title;
import touch.baton.domain.common.vo.WatchedCount;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.exception.RunnerPostDomainException;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.RunnerPostTags;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class RunnerPost extends BaseEntity {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Contents contents;

    @Embedded
    private PullRequestUrl pullRequestUrl;

    @Embedded
    private Deadline deadline;

    @Embedded
    private WatchedCount watchedCount;

    @Embedded
    private ChattingCount chattingCount;

    @Enumerated(STRING)
    @Column(nullable = false)
    private ReviewStatus reviewStatus = ReviewStatus.NOT_STARTED;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "runner_id", foreignKey = @ForeignKey(name = "fk_runner_post_to_runner"), nullable = false)
    private Runner runner;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "supporter_id", foreignKey = @ForeignKey(name = "fk_runner_post_to_supporter"), nullable = true)
    private Supporter supporter;

    @Embedded
    private RunnerPostTags runnerPostTags;

    @Builder
    private RunnerPost(final Title title,
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
        this(null, title, contents, pullRequestUrl, deadline, watchedCount, chattingCount, reviewStatus, runner, supporter, runnerPostTags);
    }

    private RunnerPost(final Long id,
                       final Title title,
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
        validateNotNull(title, contents, pullRequestUrl, deadline, watchedCount, chattingCount, reviewStatus, runner, runnerPostTags);
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.pullRequestUrl = pullRequestUrl;
        this.deadline = deadline;
        this.watchedCount = watchedCount;
        this.chattingCount = chattingCount;
        this.reviewStatus = reviewStatus;
        this.runner = runner;
        this.supporter = supporter;
        this.runnerPostTags = runnerPostTags;
    }

    private void validateNotNull(final Title title,
                                 final Contents contents,
                                 final PullRequestUrl pullRequestUrl,
                                 final Deadline deadline,
                                 final WatchedCount watchedCount,
                                 final ChattingCount chattingCount,
                                 final ReviewStatus reviewStatus,
                                 final Runner runner,
                                 final RunnerPostTags runnerPostTags
    ) {
        if (Objects.isNull(title)) {
            throw new RunnerPostDomainException("RunnerPost 의 title 은 null 일 수 없습니다.");
        }

        if (Objects.isNull(contents)) {
            throw new RunnerPostDomainException("RunnerPost 의 contents 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(pullRequestUrl)) {
            throw new RunnerPostDomainException("RunnerPost 의 pullRequestUrl 은 null 일 수 없습니다.");
        }

        if (Objects.isNull(deadline)) {
            throw new RunnerPostDomainException("RunnerPost 의 deadline 은 null 일 수 없습니다.");
        }

        if (Objects.isNull(watchedCount)) {
            throw new RunnerPostDomainException("RunnerPost 의 watchedCount 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(chattingCount)) {
            throw new RunnerPostDomainException("RunnerPost 의 chattingCount 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(reviewStatus)) {
            throw new RunnerPostDomainException("RunnerPost 의 reviewStatus 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(runner)) {
            throw new RunnerPostDomainException("RunnerPost 의 runner 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(runnerPostTags)) {
            throw new RunnerPostDomainException("RunnerPost 의 runnerPostTags 는 null 일 수 없습니다.");
        }
    }

    public static RunnerPost newInstance(final String title,
                                         final String contents,
                                         final String pullRequestUrl,
                                         final LocalDateTime deadline,
                                         final Runner runner
    ) {
        return RunnerPost.builder()
                .title(new Title(title))
                .contents(new Contents(contents))
                .pullRequestUrl(new PullRequestUrl(pullRequestUrl))
                .deadline(new Deadline(deadline))
                .runner(runner)
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .watchedCount(WatchedCount.zero())
                .reviewStatus(ReviewStatus.NOT_STARTED)
                .chattingCount(ChattingCount.zero())
                .build();
    }

    public void addAllRunnerPostTags(final List<RunnerPostTag> postTags) {
        runnerPostTags.addAll(postTags);
    }

    public void appendRunnerPostTag(RunnerPostTag postTag) {
        runnerPostTags.add(postTag);
    }

    public void updateTitle(final Title title) {
        this.title = title;
    }

    public void updateContents(final Contents contents) {
        this.contents = contents;
    }

    public void updatePullRequestUrl(final PullRequestUrl pullRequestUrl) {
        this.pullRequestUrl = pullRequestUrl;
    }

    public void updateDeadLine(final Deadline deadline) {
        this.deadline = deadline;
    }

    public void assignSupporter(final Supporter supporter) {
        this.supporter = supporter;
    }

    public void increaseWatchedCount() {
        this.watchedCount = watchedCount.increase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RunnerPost that = (RunnerPost) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
