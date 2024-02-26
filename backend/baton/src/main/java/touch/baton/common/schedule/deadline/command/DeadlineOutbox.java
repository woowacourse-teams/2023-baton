package touch.baton.common.schedule.deadline.command;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import touch.baton.common.schedule.deadline.command.exception.DeadlineOutboxException;
import touch.baton.domain.common.BaseEntity;

import java.time.Instant;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class DeadlineOutbox extends BaseEntity {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    private Long runnerPostId;

    private Instant instantToRun;

    @Builder
    public DeadlineOutbox(final Long runnerPostId, final Instant instantToRun) {
        validateNotNull(runnerPostId, instantToRun);
        this.runnerPostId = runnerPostId;
        this.instantToRun = instantToRun;
    }

    private void validateNotNull(final Long runnerPostId, final Instant instantToRun) {
        if (runnerPostId == null) {
            throw new DeadlineOutboxException("DeadlineOutBox의 runnerPostId는 null이 될 수 없습니다.");
        }

        if (instantToRun == null) {
            throw new DeadlineOutboxException("DeadlineOutBox의 instantToRun은 null이 될 수 없습니다.");
        }
    }
}
