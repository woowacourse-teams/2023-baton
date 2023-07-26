package touch.baton.domain.runnerpost.vo;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class IsOwner {

    private boolean value;

    public IsOwner(final boolean value) {
        validateOwner(value);
        this.value = value;
    }

    private void validateOwner(final boolean value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("isOwner 은 null 일 수 없습니다.");
        }
    }

    public static boolean checkIsOwner(final Long runnerPostRunnerId, final Long runnerId) {
        if(runnerPostRunnerId == runnerId){
            return true;
        }
        return false;
    }
}
