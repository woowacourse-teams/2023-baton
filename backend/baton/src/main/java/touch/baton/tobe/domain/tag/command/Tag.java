package touch.baton.tobe.domain.tag.command;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import touch.baton.domain.common.vo.TagName;
import touch.baton.tobe.domain.tag.command.vo.TagReducedName;
import touch.baton.tobe.domain.tag.exception.TagDomainException;

import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Tag {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Embedded
    private TagName tagName;

    @Embedded
    private TagReducedName tagReducedName;

    @Builder
    private Tag(final TagName tagName, final TagReducedName tagReducedName) {
        this(null, tagName, tagReducedName);
    }

    private Tag(final Long id, final TagName tagName, final TagReducedName tagReducedName) {
        validateNotNull(tagName, tagReducedName);
        this.id = id;
        this.tagName = tagName;
        this.tagReducedName = tagReducedName;
    }

    private void validateNotNull(final TagName tagName, final TagReducedName tagReducedName) {
        if (Objects.isNull(tagName)) {
            throw new TagDomainException("Tag 의 tagName 은 null 일 수 없습니다.");
        }
        if (Objects.isNull(tagReducedName)) {
            throw new TagDomainException("Tag 의 tagReducedName 은 null 일 수 없습니다.");
        }
    }

    public static Tag newInstance(final String tagName) {
        return Tag.builder()
                .tagName(new TagName(tagName))
                .tagReducedName(TagReducedName.from(tagName))
                .build();
    }
}
