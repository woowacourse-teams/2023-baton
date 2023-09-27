package touch.baton.domain.technicaltag.command;

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
public class SupporterTechnicalTags {

    @OneToMany(mappedBy = "supporter", cascade = PERSIST, orphanRemoval = true)
    private List<SupporterTechnicalTag> supporterTechnicalTags = new ArrayList<>();

    public SupporterTechnicalTags(final List<SupporterTechnicalTag> supporterTechnicalTags) {
        this.supporterTechnicalTags = supporterTechnicalTags;
    }

    public void addAll(final List<SupporterTechnicalTag> supporterTechnicalTags) {
        this.supporterTechnicalTags.addAll(supporterTechnicalTags);
    }
}
