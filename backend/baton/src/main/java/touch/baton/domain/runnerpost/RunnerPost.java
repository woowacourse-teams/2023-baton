package touch.baton.domain.runnerpost;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import touch.baton.domain.common.BaseEntity;
import touch.baton.domain.common.vo.ChattingRoomCount;
import touch.baton.domain.common.vo.Contents;
import touch.baton.domain.common.vo.Title;
import touch.baton.domain.common.vo.WatchedCount;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.exception.RunnerPostException;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.RunnerPostTags;

import java.util.Objects;

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
    private ChattingRoomCount chattingRoomCount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "runner_id", foreignKey = @ForeignKey(name = "fk_runner_post_runner"), nullable = false)
    private Runner runner;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "supporter_id", foreignKey = @ForeignKey(name = "fk_runner_post_supporter"), nullable = true)
    private Supporter supporter;

    @Embedded
    private RunnerPostTags runnerPostTags;

    @Builder
    private RunnerPost(final Title title,
                       final Contents contents,
                       final PullRequestUrl pullRequestUrl,
                       final Deadline deadline,
                       final WatchedCount watchedCount,
                       final ChattingRoomCount chattingRoomCount,
                       final Runner runner,
                       final Supporter supporter,
                       final RunnerPostTags runnerPostTags
    ) {
        this(null, title, contents, pullRequestUrl, deadline, watchedCount, chattingRoomCount, runner, supporter, runnerPostTags);
    }

    private RunnerPost(final Long id,
                      final Title title,
                      final Contents contents,
                      final PullRequestUrl pullRequestUrl,
                      final Deadline deadline,
                      final WatchedCount watchedCount,
                      final ChattingRoomCount chattingRoomCount,
                      final Runner runner,
                      final Supporter supporter,
                      final RunnerPostTags runnerPostTags
    ) {
        validateNotNull(title, contents, pullRequestUrl, deadline, watchedCount, chattingRoomCount, runner, runnerPostTags);
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.pullRequestUrl = pullRequestUrl;
        this.deadline = deadline;
        this.watchedCount = watchedCount;
        this.chattingRoomCount = chattingRoomCount;
        this.runner = runner;
        this.supporter = supporter;
        this.runnerPostTags = runnerPostTags;
    }

    private void validateNotNull(final Title title,
                                 final Contents contents,
                                 final PullRequestUrl pullRequestUrl,
                                 final Deadline deadline,
                                 final WatchedCount watchedCount,
                                 final ChattingRoomCount chattingRoomCount,
                                 final Runner runner,
                                 final RunnerPostTags runnerPostTags
    ) {
        if (Objects.isNull(title)) {
            throw new RunnerPostException.NotNull("title 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(contents)) {
            throw new RunnerPostException.NotNull("contents 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(pullRequestUrl)) {
            throw new RunnerPostException.NotNull("pullRequestUrl 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(deadline)) {
            throw new RunnerPostException.NotNull("deadline 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(watchedCount)) {
            throw new RunnerPostException.NotNull("watchedCount 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(chattingRoomCount)) {
            throw new RunnerPostException.NotNull("chattingRoomCount 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(runner)) {
            throw new RunnerPostException.NotNull("runner 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(runnerPostTags)) {
            throw new RunnerPostException.NotNull("runnerPostTags 는 null 일 수 없습니다.");
        }
    }

    public void appendRunnerPostTag(RunnerPostTag runnerPostTag) {
        runnerPostTags.add(runnerPostTag);
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
}
