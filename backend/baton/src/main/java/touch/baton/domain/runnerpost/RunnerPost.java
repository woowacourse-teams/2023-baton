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
import static touch.baton.domain.runnerpost.vo.ReviewStatus.*;

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
                       final ReviewStatus reviewStatus,
                       final Runner runner,
                       final Supporter supporter,
                       final RunnerPostTags runnerPostTags
    ) {
        this(null, title, contents, pullRequestUrl, deadline, watchedCount, reviewStatus, runner, supporter, runnerPostTags);
    }

    private RunnerPost(final Long id,
                       final Title title,
                       final Contents contents,
                       final PullRequestUrl pullRequestUrl,
                       final Deadline deadline,
                       final WatchedCount watchedCount,
                       final ReviewStatus reviewStatus,
                       final Runner runner,
                       final Supporter supporter,
                       final RunnerPostTags runnerPostTags
    ) {
        validateNotNull(title, contents, pullRequestUrl, deadline, watchedCount, reviewStatus, runner, runnerPostTags);
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.pullRequestUrl = pullRequestUrl;
        this.deadline = deadline;
        this.watchedCount = watchedCount;
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
                .reviewStatus(NOT_STARTED)
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

    public void updateReviewStatus(final ReviewStatus other) {
        if (this.reviewStatus.isSame(NOT_STARTED) && other.isSame(IN_PROGRESS)) {
            throw new RunnerPostDomainException("ReviewStatus 를 수정하던 도중 NOT_STARTED 에서 IN_PROGRESS 로 리뷰 상태 정책을 원인으로 실패하였습니다.");
        }
        if (this.reviewStatus.isSame(NOT_STARTED) && other.isSame(DONE)) {
            throw new RunnerPostDomainException("ReviewStatus 를 수정하던 도중 NOT_STARTED 에서 DONE 으로 리뷰 상태 정책을 원인으로 실패하였습니다.");
        }
        if (this.reviewStatus.isSame(DONE) && other.isSame(NOT_STARTED)) {
            throw new RunnerPostDomainException("ReviewStatus 를 수정하던 도중 DONE 에서 NOT_STARTED 로 리뷰 상태 정책을 원인으로 실패하였습니다.");
        }
        if (this.reviewStatus.isSame(DONE) && other.isSame(IN_PROGRESS)) {
            throw new RunnerPostDomainException("ReviewStatus 를 수정하던 도중 DONE 에서 IN_PROGRESS 로 리뷰 상태 정책을 원인으로 실패하였습니다.");
        }
        if (this.reviewStatus.isSame(DONE) && other.isSame(OVERDUE)) {
            throw new RunnerPostDomainException("ReviewStatus 를 수정하던 도중 DONE 에서 OVERDUE 로 리뷰 상태 정책을 원인으로 실패하였습니다.");
        }
        if (this.reviewStatus.isSame(other)) {
            throw new RunnerPostDomainException("ReviewStatus 를 수정하던 도중 같은 ReviewStatus 로 리뷰 상태 정책을 원인으로 실패하였습니다.");
        }

        this.reviewStatus = other;
    }

    public void assignSupporter(final Supporter supporter) {
        if (Objects.nonNull(this.supporter)) {
            throw new RunnerPostDomainException("Supporter 를 할당하던 도중 RunnerPost 에 이미 다른 Supporter 가 할당되어 있는 것을 원인으로 실패하였습니다.");
        }
        if (reviewStatus.isSame(OVERDUE)) {
            throw new RunnerPostDomainException("Supporter 를 할당하던 도중 ReviewStatus 가 OVERDUE 상태가 원인으로 실패하였습니다.");
        }
        if (reviewStatus.isNotSameAsNotStarted()) {
            throw new RunnerPostDomainException("Supporter 를 할당하던 도중 ReviewStatus 가 NOT_STARTED 상태가 아닌 것을 원인으로 실패하였습니다.");
        }
        if (deadline.isEnd()) {
            throw new RunnerPostDomainException("Supporter 를 할당하던 도중 ReviewStatus 의 Deadline 이 현재 시간보다 과거인 것을 원인으로 실패하였습니다.");
        }

        this.supporter = supporter;
        this.reviewStatus = IN_PROGRESS;
    }

    public void increaseWatchedCount() {
        this.watchedCount = watchedCount.increase();
    }

    public boolean isNotOwner(final Runner targetRunner) {
        return !runner.equals(targetRunner);
    }

    public boolean isReviewStatusStarted() {
        return reviewStatus.isNotSameAsNotStarted();
    }

    public boolean isDifferentSupporter(final Supporter targetSupporter) {
        return !supporter.equals(targetSupporter);
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
