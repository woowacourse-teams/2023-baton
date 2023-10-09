package touch.baton.domain.notification.command;

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
import touch.baton.domain.common.TruncatedBaseEntity;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.notification.command.vo.IsRead;
import touch.baton.domain.notification.command.vo.NotificationMessage;
import touch.baton.domain.notification.command.vo.NotificationReferencedId;
import touch.baton.domain.notification.command.vo.NotificationTitle;
import touch.baton.domain.notification.command.vo.NotificationType;
import touch.baton.domain.notification.exception.NotificationDomainException;

import java.util.Objects;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE notification SET deleted_at = now() WHERE id = ?")
@Entity
public class Notification extends TruncatedBaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Embedded
    private NotificationTitle notificationTitle;

    @Embedded
    private NotificationMessage notificationMessage;

    @Enumerated(STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    @Embedded
    private NotificationReferencedId notificationReferencedId;

    @Embedded
    private IsRead isRead;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_notification_to_member"))
    private Member member;

    @Builder
    private Notification(final NotificationTitle notificationTitle,
                         final NotificationMessage notificationMessage,
                         final NotificationType notificationType,
                         final NotificationReferencedId notificationReferencedId,
                         final IsRead isRead,
                         final Member member
    ) {
        this(null, notificationTitle, notificationMessage, notificationType, notificationReferencedId, isRead, member);
    }

    private Notification(final Long id,
                         final NotificationTitle notificationTitle,
                         final NotificationMessage notificationMessage,
                         final NotificationType notificationType,
                         final NotificationReferencedId notificationReferencedId,
                         final IsRead isRead,
                         final Member member
    ) {
        validateNotNull(notificationTitle, notificationMessage, notificationType, notificationReferencedId, isRead, member);
        this.id = id;
        this.notificationTitle = notificationTitle;
        this.notificationMessage = notificationMessage;
        this.notificationType = notificationType;
        this.notificationReferencedId = notificationReferencedId;
        this.isRead = isRead;
        this.member = member;
    }

    private void validateNotNull(final NotificationTitle notificationTitle,
                                 final NotificationMessage notificationMessage,
                                 final NotificationType notificationType,
                                 final NotificationReferencedId notificationReferencedId,
                                 final IsRead isRead,
                                 final Member member
    ) {
        if (notificationTitle == null) {
            throw new NotificationDomainException("NotificationTitle 의 notificationTitle 은 null 일 수 없습니다.");
        }
        if (notificationMessage == null) {
            throw new NotificationDomainException("NotificationMessage 의 notificationMessage 는 null 일 수 없습니다.");
        }
        if (notificationType == null) {
            throw new NotificationDomainException("NotificationType 의 notificationType 는 null 일 수 없습니다.");
        }
        if (notificationReferencedId == null) {
            throw new NotificationDomainException("NotificationReferencedId 의 notificationReferencedId 은 null 일 수 없습니다.");
        }
        if (isRead == null) {
            throw new NotificationDomainException("IsRead 의 isRead 는 null 일 수 없습니다.");
        }
        if (member == null) {
            throw new NotificationDomainException("Member 의 member 는 null 일 수 없습니다.");
        }
    }

    public void markAsRead(final Member currentMember) {
        if (!this.member.equals(currentMember)) {
            throw new NotificationDomainException("Notification 의 주인(사용자)가 아니므로 알림의 읽은 여부를 수정할 수 없습니다.");
        }

        this.isRead = IsRead.asRead();
    }

    public boolean isNotOwner(final Member currentMember) {
        return !this.member.equals(currentMember);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Notification notification = (Notification) o;
        return Objects.equals(id, notification.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
