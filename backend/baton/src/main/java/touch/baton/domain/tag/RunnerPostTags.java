package touch.baton.domain.tag;

import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    public void add(final RunnerPostTag postTag) {
        runnerPostTags.add(postTag);
    }
}
