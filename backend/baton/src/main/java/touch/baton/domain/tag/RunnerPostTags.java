package touch.baton.domain.tag;

import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static jakarta.persistence.CascadeType.PERSIST;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class RunnerPostTags {

    @OneToMany(mappedBy = "runnerPost", cascade = PERSIST, orphanRemoval = true)
    private List<RunnerPostTag> runnerPostTags = new ArrayList<>();

    public RunnerPostTags(final List<RunnerPostTag> runnerPostTags) {
        this.runnerPostTags = runnerPostTags;
    }

    public void add(final RunnerPostTag runnerPostTag) {
        runnerPostTags.add(runnerPostTag);
    }

    public void addAll(final List<RunnerPostTag> runnerPostTags) {
        this.runnerPostTags.addAll(runnerPostTags);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final RunnerPostTags that = (RunnerPostTags) o;
        return Objects.equals(runnerPostTags, that.runnerPostTags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(runnerPostTags);
    }
}
