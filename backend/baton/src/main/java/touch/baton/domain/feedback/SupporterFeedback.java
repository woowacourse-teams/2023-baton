package touch.baton.domain.feedback;

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
import touch.baton.domain.feedback.exception.SupporterFeedbackException;
import touch.baton.domain.feedback.vo.Description;
import touch.baton.domain.feedback.vo.ReviewType;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.supporter.Supporter;

import java.util.Objects;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class SupporterFeedback extends BaseEntity {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Enumerated(STRING)
    @Column(nullable = false)
    private ReviewType reviewType;

    @Embedded
    private Description description;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "supporter_id", nullable = false, foreignKey = @ForeignKey(name = "fk_supporter_feed_back_to_supporter"))
    private Supporter supporter;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "runner_id", nullable = false, foreignKey = @ForeignKey(name = "fk_supporter_feed_back_to_runner"))
    private Runner runner;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "runner_post_id", nullable = false, foreignKey = @ForeignKey(name = "fk_supporter_feed_back_to_runner_post"))
    private RunnerPost runnerPost;

    @Builder
    private SupporterFeedback(final ReviewType reviewType, final Description description, final Supporter supporter, final Runner runner, final RunnerPost runnerPost) {
        this(null, reviewType, description, supporter, runner, runnerPost);
    }

    private SupporterFeedback(final Long id, final ReviewType reviewType, final Description description, final Supporter supporter, final Runner runner, final RunnerPost runnerPost) {
        validateNotNull(reviewType, description, supporter, runner, runnerPost);
        this.id = id;
        this.reviewType = reviewType;
        this.description = description;
        this.supporter = supporter;
        this.runner = runner;
        this.runnerPost = runnerPost;
    }

    private void validateNotNull(final ReviewType reviewType,
                                 final Description description,
                                 final Supporter supporter,
                                 final Runner runner,
                                 final RunnerPost runnerPost
    ) {
        if (Objects.isNull(reviewType)) {
            throw new SupporterFeedbackException("SupporterFeedback 의 reviewType 은 null 일 수 없습니다.");
        }

        if (Objects.isNull(description)) {
            throw new SupporterFeedbackException("SupporterFeedback 의 description 은 null 일 수 없습니다.");
        }

        if (Objects.isNull(supporter)) {
            throw new SupporterFeedbackException("SupporterFeedback 의 supporter 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(runner)) {
            throw new SupporterFeedbackException("SupporterFeedback 의 runner 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(runnerPost)) {
            throw new SupporterFeedbackException("SupporterFeedback 의 runnerPost 는 null 일 수 없습니다.");
        }
    }
}
