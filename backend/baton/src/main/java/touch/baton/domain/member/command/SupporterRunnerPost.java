package touch.baton.domain.member.command;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import touch.baton.domain.common.BaseEntity;
import touch.baton.domain.member.command.vo.Message;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.exception.RunnerPostDomainException;

import java.util.Objects;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class SupporterRunnerPost extends BaseEntity {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Embedded
    private Message message;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "supporter_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_support_runner_post_to_supporter"))
    private Supporter supporter;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "runner_post_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_support_runner_post_to_runner_post"))
    private RunnerPost runnerPost;

    @Builder
    private SupporterRunnerPost(final Message message,
                                final Supporter supporter,
                                final RunnerPost runnerPost
    ) {
        this(null, message, supporter, runnerPost);
    }

    private SupporterRunnerPost(final Long id, final Message message, final Supporter supporter, final RunnerPost runnerPost) {
        validateNotNull(message, supporter, runnerPost);
        this.id = id;
        this.message = message;
        this.supporter = supporter;
        this.runnerPost = runnerPost;
    }

    private void validateNotNull(final Message message,
                                 final Supporter supporter,
                                 final RunnerPost runnerPost
    ) {
        if (Objects.isNull(message)) {
            throw new RunnerPostDomainException("SupporterRunnerPost 의 message 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(supporter)) {
            throw new RunnerPostDomainException("SupporterRunnerPost 의 supporter 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(runnerPost)) {
            throw new RunnerPostDomainException("SupporterRunnerPost 의 runnerPost 는 null 일 수 없습니다.");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof SupporterRunnerPost that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
