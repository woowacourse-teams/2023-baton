package touch.baton.domain.alarm.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import touch.baton.domain.alarm.exception.AlarmDomainException;
import touch.baton.domain.alarm.command.vo.AlarmMessage;
import touch.baton.domain.alarm.command.vo.AlarmTitle;
import touch.baton.domain.alarm.command.vo.AlarmReferencedId;
import touch.baton.domain.alarm.command.vo.AlarmType;
import touch.baton.domain.alarm.command.vo.IsRead;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.vo.Company;
import touch.baton.domain.member.command.vo.GithubUrl;
import touch.baton.domain.member.command.vo.ImageUrl;
import touch.baton.domain.member.command.vo.MemberName;
import touch.baton.domain.member.command.vo.OauthId;
import touch.baton.domain.member.command.vo.SocialId;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AlarmTest {

    @DisplayName("생성 테스트")
    @Nested
    class Create {

        private static final Member member = Member.builder()
                .memberName(new MemberName("사용자 테스트용 이름"))
                .socialId(new SocialId("사용자 테스트용 소셜 아이디"))
                .oauthId(new OauthId("사용자 테스트용 오어스 아이디"))
                .githubUrl(new GithubUrl("https://github.com/사용자_테스트용_깃허브_주소"))
                .company(new Company("사용자 테스트용 회사명"))
                .imageUrl(new ImageUrl("https://사용자_테스트용_이미지_주소"))
                .build();

        @DisplayName("성공한다")
        @Test
        void success() {
            assertThatCode(() -> Alarm.builder()
                    .alarmTitle(new AlarmTitle("알람 테스트용 제목"))
                    .alarmMessage(new AlarmMessage("알람 테스트용 내용"))
                    .alarmType(AlarmType.RUNNER_POST)
                    .alarmReferencedId(new AlarmReferencedId(1L))
                    .isRead(new IsRead(false))
                    .member(member)
                    .build()
            ).doesNotThrowAnyException();
        }

        @DisplayName("alarmTitle 에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_alarmTitle_is_null() {
            assertThatThrownBy(() -> Alarm.builder()
                    .alarmTitle(null)
                    .alarmMessage(new AlarmMessage("알람 테스트용 내용"))
                    .alarmType(AlarmType.RUNNER_POST)
                    .alarmReferencedId(new AlarmReferencedId(1L))
                    .isRead(new IsRead(false))
                    .member(member)
                    .build()
            ).isInstanceOf(AlarmDomainException.class);
        }

        @DisplayName("alarmMessage 에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_alarmMessage_is_null() {
            assertThatThrownBy(() -> Alarm.builder()
                    .alarmTitle(new AlarmTitle("알람 테스트용 제목"))
                    .alarmMessage(null)
                    .alarmType(AlarmType.RUNNER_POST)
                    .alarmReferencedId(new AlarmReferencedId(1L))
                    .isRead(new IsRead(false))
                    .member(member)
                    .build()
            ).isInstanceOf(AlarmDomainException.class);
        }

        @DisplayName("alarmType() 에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_alarmType_is_null() {
            assertThatThrownBy(() -> Alarm.builder()
                    .alarmTitle(new AlarmTitle("알람 테스트용 제목"))
                    .alarmMessage(new AlarmMessage("알람 테스트용 내용"))
                    .alarmType(null)
                    .alarmReferencedId(new AlarmReferencedId(1L))
                    .isRead(new IsRead(false))
                    .member(member)
                    .build()
            ).isInstanceOf(AlarmDomainException.class);
        }

        @DisplayName("alarmReferencedId() 에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_alarmReferencedId_is_null() {
            assertThatThrownBy(() -> Alarm.builder()
                    .alarmTitle(new AlarmTitle("알람 테스트용 제목"))
                    .alarmMessage(new AlarmMessage("알람 테스트용 내용"))
                    .alarmType(AlarmType.RUNNER_POST)
                    .alarmReferencedId(null)
                    .isRead(new IsRead(false))
                    .member(member)
                    .build()
            ).isInstanceOf(AlarmDomainException.class);
        }

        @DisplayName("isRead 에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_isRead_is_null() {
            assertThatThrownBy(() -> Alarm.builder()
                    .alarmTitle(new AlarmTitle("알람 테스트용 제목"))
                    .alarmMessage(new AlarmMessage("알람 테스트용 내용"))
                    .alarmType(AlarmType.RUNNER_POST)
                    .alarmReferencedId(new AlarmReferencedId(1L))
                    .isRead(null)
                    .member(member)
                    .build()
            ).isInstanceOf(AlarmDomainException.class);
        }

        @DisplayName("member 에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_member_is_null() {
            assertThatThrownBy(() -> Alarm.builder()
                    .alarmTitle(new AlarmTitle("알람 테스트용 제목"))
                    .alarmMessage(new AlarmMessage("알람 테스트용 내용"))
                    .alarmType(AlarmType.RUNNER_POST)
                    .alarmReferencedId(new AlarmReferencedId(1L))
                    .isRead(new IsRead(false))
                    .member(null)
                    .build()
            ).isInstanceOf(AlarmDomainException.class);
        }
    }
}
