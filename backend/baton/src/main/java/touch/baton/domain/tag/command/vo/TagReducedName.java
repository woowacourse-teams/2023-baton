package touch.baton.domain.tag.command.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class TagReducedName {

    private static final String BLANK = " ";
    private static final String NOT_BLANK = "";

    @Column(name = "reduced_name", nullable = false)
    private String value;

    private TagReducedName(final String value) {
        this.value = value;
    }

    public static TagReducedName nullableInstance(final String notReducedValue) {
        if (notReducedValue == null) {
            return null;
        }
        return new TagReducedName(reduceName(notReducedValue));
    }

    public static TagReducedName from(final String notReducedValue) {
        validateNotNull(notReducedValue);
        final String reducedValue = reduceName(notReducedValue);
        return new TagReducedName(reducedValue);
    }

    private static void validateNotNull(final String notReducedValue) {
        if (Objects.isNull(notReducedValue)) {
            throw new IllegalArgumentException("TagReducedName 객체를 생성할 때 notReducedValue 은 null 일 수 없습니다.");
        }
    }

    private static String reduceName(final String beforeReduced) {
        final String afterLowerCase = beforeReduced.toLowerCase();
        final String afterReduceBlank = afterLowerCase.replaceAll(BLANK, NOT_BLANK);
        return afterReduceBlank;
    }
}
