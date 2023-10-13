package touch.baton.domain.common;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public abstract class TruncatedBaseEntity extends BaseEntity {

    @Override
    public LocalDateTime getCreatedAt() {
        return super.getCreatedAt().truncatedTo(ChronoUnit.MINUTES);
    }

    @Override
    public LocalDateTime getDeletedAt() {
        return super.getDeletedAt().truncatedTo(ChronoUnit.MINUTES);
    }

    @Override
    public LocalDateTime getUpdatedAt() {
        return super.getUpdatedAt().truncatedTo(ChronoUnit.MINUTES);
    }
}
