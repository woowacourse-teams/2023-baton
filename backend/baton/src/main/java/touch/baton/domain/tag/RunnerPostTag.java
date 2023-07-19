package touch.baton.domain.tag;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.tag.exception.TagException;

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
    @JoinColumn(name = "runner_post_id", nullable = false)
    private RunnerPost runnerPost;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
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
            throw new TagException.NotNull("runnerPost 은 null 일 수 없습니다.");
        }

        if (Objects.isNull(tag)) {
            throw new TagException.NotNull("tag 은 null 일 수 없습니다.");
        }
    }

    public static RunnerPostTag create(final RunnerPost runnerPost, final Tag tag) {
        return RunnerPostTag.builder()
                .runnerPost(runnerPost)
                .tag(tag)
                .build();
    }
}
