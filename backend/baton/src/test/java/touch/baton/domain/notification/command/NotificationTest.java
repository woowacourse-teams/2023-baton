package touch.baton.domain.notification.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.vo.Company;
import touch.baton.domain.member.command.vo.GithubUrl;
import touch.baton.domain.member.command.vo.ImageUrl;
import touch.baton.domain.member.command.vo.MemberName;
import touch.baton.domain.member.command.vo.OauthId;
import touch.baton.domain.member.command.vo.SocialId;
import touch.baton.domain.notification.command.vo.IsRead;
import touch.baton.domain.notification.command.vo.NotificationMessage;
import touch.baton.domain.notification.command.vo.NotificationReferencedId;
import touch.baton.domain.notification.command.vo.NotificationTitle;
import touch.baton.domain.notification.command.vo.NotificationType;
import touch.baton.domain.notification.exception.NotificationDomainException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class NotificationTest {

    private static final Member owner = Member.builder()
            .memberName(new MemberName("사용자 테스트용 이름"))
            .socialId(new SocialId("사용자 테스트용 소셜 아이디"))
            .oauthId(new OauthId("사용자 테스트용 오어스 아이디"))
            .githubUrl(new GithubUrl("https://github.com/사용자_테스트용_깃허브_주소"))
            .company(new Company("사용자 테스트용 회사명"))
            .imageUrl(new ImageUrl("https://사용자_테스트용_이미지_주소"))
            .build();

    private static final Member notOwner = Member.builder()
            .memberName(new MemberName("사용자 테스트용 이름"))
            .socialId(new SocialId("사용자 테스트용 소셜 아이디"))
            .oauthId(new OauthId("사용자 테스트용 오어스 아이디"))
            .githubUrl(new GithubUrl("https://github.com/사용자_테스트용_깃허브_주소"))
            .company(new Company("사용자 테스트용 회사명"))
            .imageUrl(new ImageUrl("https://사용자_테스트용_이미지_주소"))
            .build();

    @DisplayName("생성 테스트")
    @Nested
    class Create {

        @DisplayName("성공한다")
        @Test
        void success() {
            assertThatCode(() -> Notification.builder()
                    .notificationTitle(new NotificationTitle("알림 테스트용 제목"))
                    .notificationMessage(new NotificationMessage("알림 테스트용 내용"))
                    .notificationType(NotificationType.RUNNER_POST)
                    .notificationReferencedId(new NotificationReferencedId(1L))
                    .isRead(IsRead.asUnRead())
                    .member(owner)
                    .build()
            ).doesNotThrowAnyException();
        }

        @DisplayName("notificationTitle 에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_notificationTitle_is_null() {
            assertThatThrownBy(() -> Notification.builder()
                    .notificationTitle(null)
                    .notificationMessage(new NotificationMessage("알림 테스트용 내용"))
                    .notificationType(NotificationType.RUNNER_POST)
                    .notificationReferencedId(new NotificationReferencedId(1L))
                    .isRead(IsRead.asUnRead())
                    .member(owner)
                    .build()
            ).isInstanceOf(NotificationDomainException.class);
        }

        @DisplayName("notificationMessage 에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_notificationMessage_is_null() {
            assertThatThrownBy(() -> Notification.builder()
                    .notificationTitle(new NotificationTitle("알림 테스트용 제목"))
                    .notificationMessage(null)
                    .notificationType(NotificationType.RUNNER_POST)
                    .notificationReferencedId(new NotificationReferencedId(1L))
                    .isRead(IsRead.asUnRead())
                    .member(owner)
                    .build()
            ).isInstanceOf(NotificationDomainException.class);
        }

        @DisplayName("notificationType() 에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_notificationType_is_null() {
            assertThatThrownBy(() -> Notification.builder()
                    .notificationTitle(new NotificationTitle("알림 테스트용 제목"))
                    .notificationMessage(new NotificationMessage("알림 테스트용 내용"))
                    .notificationType(null)
                    .notificationReferencedId(new NotificationReferencedId(1L))
                    .isRead(IsRead.asUnRead())
                    .member(owner)
                    .build()
            ).isInstanceOf(NotificationDomainException.class);
        }

        @DisplayName("notificationReferencedId() 에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_notificationReferencedId_is_null() {
            assertThatThrownBy(() -> Notification.builder()
                    .notificationTitle(new NotificationTitle("알림 테스트용 제목"))
                    .notificationMessage(new NotificationMessage("알림 테스트용 내용"))
                    .notificationType(NotificationType.RUNNER_POST)
                    .notificationReferencedId(null)
                    .isRead(IsRead.asUnRead())
                    .member(owner)
                    .build()
            ).isInstanceOf(NotificationDomainException.class);
        }

        @DisplayName("isRead 에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_isRead_is_null() {
            assertThatThrownBy(() -> Notification.builder()
                    .notificationTitle(new NotificationTitle("알림 테스트용 제목"))
                    .notificationMessage(new NotificationMessage("알림 테스트용 내용"))
                    .notificationType(NotificationType.RUNNER_POST)
                    .notificationReferencedId(new NotificationReferencedId(1L))
                    .isRead(null)
                    .member(owner)
                    .build()
            ).isInstanceOf(NotificationDomainException.class);
        }

        @DisplayName("member 에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_member_is_null() {
            assertThatThrownBy(() -> Notification.builder()
                    .notificationTitle(new NotificationTitle("알림 테스트용 제목"))
                    .notificationMessage(new NotificationMessage("알림 테스트용 내용"))
                    .notificationType(NotificationType.RUNNER_POST)
                    .notificationReferencedId(new NotificationReferencedId(1L))
                    .isRead(IsRead.asUnRead())
                    .member(null)
                    .build()
            ).isInstanceOf(NotificationDomainException.class);
        }
    }
}
