package touch.baton.domain.technicaltag;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import touch.baton.domain.common.vo.TagName;
import touch.baton.domain.tag.exception.TechnicalTagDomainException;

import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class TechnicalTag {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Embedded
    private TagName tagName;

    @Builder
    private TechnicalTag(final TagName tagName) {
        this(null, tagName);
    }

    private TechnicalTag(final Long id, final TagName tagName) {
        validateNotNull(tagName);
        this.id = id;
        this.tagName = tagName;
    }

    private void validateNotNull(final TagName tagName) {
        if (Objects.isNull(tagName)) {
            throw new TechnicalTagDomainException("TechnicalTag 의 tagName 은 null 일 수 없습니다.");
        }
    }
}
