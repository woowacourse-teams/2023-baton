package touch.baton.assure.notification.query;

import org.junit.jupiter.api.Test;
import touch.baton.assure.notification.support.query.NotificationQuerySupport;
import touch.baton.assure.runnerpost.support.command.RunnerPostCreateSupport;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.FakeAuthCodes;
import touch.baton.domain.notification.command.Notification;
import touch.baton.domain.notification.command.vo.NotificationMessage;
import touch.baton.domain.notification.command.vo.NotificationReferencedId;
import touch.baton.domain.notification.command.vo.NotificationTitle;
import touch.baton.domain.notification.command.vo.NotificationType;
import touch.baton.domain.notification.command.vo.IsRead;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.runnerpost.command.service.dto.RunnerPostCreateRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
class NotificationQueryAssuredTest extends AssuredTestConfig {

    @Test
    void 로그인한_사용자의_알림_목록을_조회한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(FakeAuthCodes.hyenaAuthCode());

        final RunnerPostCreateRequest 게시글_생성_요청 = new RunnerPostCreateRequest(
                "테스트용 게시글 제목",
                List.of("테스트용 태그1", "테스트용 태그2"),
                "https://github.com/test",
                LocalDateTime.now().plusDays(10),
                "테스트용 구현 내용",
                "테스트용 궁금한 내용",
                "테스트용 남길 내용"
        );

        final Long 생성된_러너_게시글_식별자값 = RunnerPostCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_게시글_등록_요청한다(게시글_생성_요청)

                .서버_응답()
                .러너_게시글_생성_성공을_검증한다()
                .생성한_러너_게시글의_식별자값을_반환한다();

        // when & then
        final List<Notification> 예상된_알림_목록 = 러너_게시글_작성자에게_5개의_알림을_저장한다(생성된_러너_게시글_식별자값);

        NotificationQuerySupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .로그인한_사용자의_알림_목록을_조회한다()

                .서버_응답()
                .로그인한_사용자의_알림_목록_조회_성공을_검증한다(예상된_알림_목록);
    }

    private List<Notification> 러너_게시글_작성자에게_5개의_알림을_저장한다(final Long 생성된_러너_게시글_식별자값) {
        final List<Notification> 예상된_알림_목록 = new ArrayList<>();
        for (int 저장될_알림_카운트_수 = 1; 저장될_알림_카운트_수 <= 5; 저장될_알림_카운트_수++) {
            final Notification 저장된_알림 = 러너_게시글_작성자에게_알림을_저장한다(생성된_러너_게시글_식별자값);
            예상된_알림_목록.add(저장된_알림);
        }
        Collections.sort(예상된_알림_목록, 알림_식별자값을_기준_내림차순으로_정렬한다());

        return 예상된_알림_목록;
    }

    private Notification 러너_게시글_작성자에게_알림을_저장한다(final Long 생성된_러너_게시글_식별자값) {
        final Member 헤나_사용자 = memberRepository.getAsRunnerByRunnerPostId(생성된_러너_게시글_식별자값);

        final Notification 저장되지_않은_알림 = Notification.builder()
                .notificationTitle(new NotificationTitle("알림 테스트용 제목"))
                .notificationMessage(new NotificationMessage("알림 테스트용 내용"))
                .notificationType(NotificationType.RUNNER_POST)
                .notificationReferencedId(new NotificationReferencedId(1L))
                .isRead(IsRead.asUnRead())
                .member(헤나_사용자)
                .build();

        return notificationCommandRepository.save(저장되지_않은_알림);
    }

    private Comparator<Notification> 알림_식별자값을_기준_내림차순으로_정렬한다() {
        return (left, right) -> left.getId() < right.getId() ? 1 : -1;
    }
}
