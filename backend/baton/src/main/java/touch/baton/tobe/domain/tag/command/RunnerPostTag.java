package touch.baton.tobe.domain.tag.command;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import touch.baton.tobe.domain.runnerpost.command.RunnerPost;
import touch.baton.tobe.domain.tag.exception.RunnerPostTagDomainException;

import java.util.Objects;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class RunnerPostTag {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "runner_post_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_runner_post_tag_to_runner_post"))
    private RunnerPost runnerPost;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tag_id", nullable = false, foreignKey = @ForeignKey(name = "fk_runner_post_tag_to_tag"))
    private Tag tag;

    @Builder
    private RunnerPostTag(final RunnerPost runnerPost, final Tag tag) {
        this(null, runnerPost, tag);
    }

    private RunnerPostTag(final Long id, final RunnerPost runnerPost, final Tag tag) {
        validateNotNull(runnerPost, tag);
        this.id = id;
        this.runnerPost = runnerPost;
        this.tag = tag;
    }

    private void validateNotNull(final RunnerPost runnerPost, final Tag tag) {
        if (Objects.isNull(runnerPost)) {
            throw new RunnerPostTagDomainException("RunnerPostTag 의 runnerPost 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(tag)) {
            throw new RunnerPostTagDomainException("RunnerPostTag 의 tag 는 null 일 수 없습니다.");
        }
    }
}
