package touch.baton.domain.tag;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import touch.baton.domain.common.vo.TagName;
import touch.baton.domain.tag.exception.OldTagException;
import touch.baton.domain.tag.vo.TagCount;

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
    private TagCount tagCount;

    @Builder
    private Tag(final TagName tagName, final TagCount tagCount) {
        this(null, tagName, tagCount);
    }

    private Tag(final Long id, final TagName tagName, final TagCount tagCount) {
        validateNotNull(tagName, tagCount);
        this.id = id;
        this.tagName = tagName;
        this.tagCount = tagCount;
    }

    private void validateNotNull(final TagName tagName, final TagCount tagCount) {
        if (Objects.isNull(tagName)) {
            throw new OldTagException.NotNull("tagName 은 null 일 수 없습니다.");
        }

        if (Objects.isNull(tagCount)) {
            throw new OldTagException.NotNull("tagCount 은 null 일 수 없습니다.");
        }
    }

    public static Tag newInstance(final String tagName) {
        return Tag.builder()
                .tagName(new TagName(tagName))
                .tagCount(TagCount.init())
                .build();
    }

    public void increaseCount() {
        this.tagCount = tagCount.increase();
    }

    public void decreaseCount() {
        this.tagCount = tagCount.decrease();
    }

    public boolean isSameTagName(final String tagName) {
        return this.tagName.equals(new TagName(tagName));
    }
}
