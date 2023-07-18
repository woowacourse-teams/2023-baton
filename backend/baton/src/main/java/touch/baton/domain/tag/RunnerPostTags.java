package touch.baton.domain.tag;

import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class RunnerPostTags {

    @OneToMany(mappedBy = "runnerPost")
    private List<RunnerPostTag> runnerPostTags = new ArrayList<>();

    public RunnerPostTags(final List<RunnerPostTag> runnerPostTags) {
        this.runnerPostTags = runnerPostTags;
    }
}
