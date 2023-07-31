package touch.baton.domain.technicaltag;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.tag.exception.OldTagException;

import java.util.Objects;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class SupporterTechnicalTag {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "supporter_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_supporter_technical_tag_to_supporter"))
    private Supporter supporter;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "technical_tag_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_supporter_technical_tag_to_technical_tag"))
    private TechnicalTag technicalTag;

    @Builder
    public SupporterTechnicalTag(final Supporter supporter, final TechnicalTag technicalTag) {
        this(null, supporter, technicalTag);
    }

    private SupporterTechnicalTag(final Long id, final Supporter supporter, final TechnicalTag technicalTag) {
        validateNotNull(supporter, technicalTag);
        this.id = id;
        this.supporter = supporter;
        this.technicalTag = technicalTag;
    }

    private void validateNotNull(final Supporter supporter, final TechnicalTag technicalTag) {
        if (Objects.isNull(supporter)) {
            throw new OldTagException.NotNull("supporter 는 null 일 수 없습니다.");
        }

        if (Objects.isNull(technicalTag)) {
            throw new OldTagException.NotNull("technicalTag 는 null 일 수 없습니다.");
        }
    }
}
