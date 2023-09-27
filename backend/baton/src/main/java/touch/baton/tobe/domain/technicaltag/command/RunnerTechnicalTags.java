package touch.baton.tobe.domain.technicaltag.command;

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
public class RunnerTechnicalTags {

    @OneToMany(mappedBy = "runner", cascade = PERSIST, orphanRemoval = true)
    private List<RunnerTechnicalTag> runnerTechnicalTags = new ArrayList<>();

    public RunnerTechnicalTags(final List<RunnerTechnicalTag> runnerTechnicalTags) {
        this.runnerTechnicalTags = runnerTechnicalTags;
    }

    public void addAll(final List<RunnerTechnicalTag> runnerTechnicalTags) {
        this.runnerTechnicalTags.addAll(runnerTechnicalTags);
    }
}
