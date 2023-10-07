package touch.baton.domain.alarm.command;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import touch.baton.domain.alarm.command.vo.AlarmMessage;
import touch.baton.domain.alarm.command.vo.AlarmReferencedId;
import touch.baton.domain.alarm.command.vo.AlarmTitle;
import touch.baton.domain.alarm.command.vo.AlarmType;
import touch.baton.domain.alarm.command.vo.IsRead;
import touch.baton.domain.alarm.exception.AlarmDomainException;
import touch.baton.domain.common.TruncatedBaseEntity;
import touch.baton.domain.member.command.Member;

import java.util.Objects;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE alarm SET deleted_at = now() WHERE id = ?")
@Entity
public class Alarm extends TruncatedBaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Embedded
    private AlarmTitle alarmTitle;

    @Embedded
    private AlarmMessage alarmMessage;

    @Enumerated(STRING)
    @Column(nullable = false)
    private AlarmType alarmType;

    @Embedded
    private AlarmReferencedId alarmReferencedId;

    @Embedded
    private IsRead isRead;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_alarm_to_member"))
    private Member member;

    @Builder
    private Alarm(final AlarmTitle alarmTitle,
                 final AlarmMessage alarmMessage,
                 final AlarmType alarmType,
                 final AlarmReferencedId alarmReferencedId,
                 final IsRead isRead,
                 final Member member
    ) {
        this(null, alarmTitle, alarmMessage, alarmType, alarmReferencedId, isRead, member);
    }

    private Alarm(final Long id,
                  final AlarmTitle alarmTitle,
                  final AlarmMessage alarmMessage,
                  final AlarmType alarmType,
                  final AlarmReferencedId alarmReferencedId,
                  final IsRead isRead,
                  final Member member
    ) {
        validateNotNull(alarmTitle, alarmMessage, alarmType, alarmReferencedId, isRead, member);
        this.id = id;
        this.alarmTitle = alarmTitle;
        this.alarmMessage = alarmMessage;
        this.alarmType = alarmType;
        this.alarmReferencedId = alarmReferencedId;
        this.isRead = isRead;
        this.member = member;
    }

    private void validateNotNull(final AlarmTitle alarmTitle,
                                 final AlarmMessage alarmMessage,
                                 final AlarmType alarmType,
                                 final AlarmReferencedId alarmReferencedId,
                                 final IsRead isRead,
                                 final Member member
    ) {
        if (alarmTitle == null) {
            throw new AlarmDomainException("AlarmTitle 의 alarmTitle 은 null 일 수 없습니다.");
        }
        if (alarmMessage == null) {
            throw new AlarmDomainException("AlarmMessage 의 alarmMessage 는 null 일 수 없습니다.");
        }
        if (alarmType == null) {
            throw new AlarmDomainException("AlarmType 의 alarmType 는 null 일 수 없습니다.");
        }
        if (alarmReferencedId == null) {
            throw new AlarmDomainException("AlarmReferencedId 의 alarmReferencedId 은 null 일 수 없습니다.");
        }
        if (isRead == null) {
            throw new AlarmDomainException("IsRead 의 isRead 는 null 일 수 없습니다.");
        }
        if (member == null) {
            throw new AlarmDomainException("Member 의 member 는 null 일 수 없습니다.");
        }
    }

    public boolean isNotOwner(final Member member) {
        return !this.member.equals(member);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Alarm alarm = (Alarm) o;
        return Objects.equals(id, alarm.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
