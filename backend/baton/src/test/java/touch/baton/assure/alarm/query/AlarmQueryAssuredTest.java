package touch.baton.assure.alarm.query;

import org.junit.jupiter.api.Test;
import touch.baton.assure.alarm.support.query.AlarmQuerySupport;
import touch.baton.assure.runnerpost.support.command.RunnerPostCreateSupport;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.MockAuthCodes;
import touch.baton.domain.alarm.command.Alarm;
import touch.baton.domain.alarm.command.vo.AlarmMessage;
import touch.baton.domain.alarm.command.vo.AlarmReferencedId;
import touch.baton.domain.alarm.command.vo.AlarmTitle;
import touch.baton.domain.alarm.command.vo.AlarmType;
import touch.baton.domain.alarm.command.vo.IsRead;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.runnerpost.command.service.dto.RunnerPostCreateRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
class AlarmQueryAssuredTest extends AssuredTestConfig {

    @Test
    void 로그인한_사용자의_알람_목록을_조회한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

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
        final List<Alarm> 예상된_알람_목록 = 러너_게시글_작성자에게_5개의_알람을_저장한다(생성된_러너_게시글_식별자값);

        AlarmQuerySupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .로그인한_사용자의_알람_목록을_조회한다()

                .서버_응답()
                .로그인한_사용자의_알람_목록_조회_성공을_검증한다(예상된_알람_목록);
    }

    private List<Alarm> 러너_게시글_작성자에게_5개의_알람을_저장한다(final Long 생성된_러너_게시글_식별자값) {
        final List<Alarm> 예상된_알람_목록 = new ArrayList<>();
        for (int 저장될_알람_카운트_수 = 1; 저장될_알람_카운트_수 <= 5; 저장될_알람_카운트_수++) {
            final Alarm 저장된_알람 = 러너_게시글_작성자에게_알람을_저장한다(생성된_러너_게시글_식별자값);
            예상된_알람_목록.add(저장된_알람);
        }
        Collections.sort(예상된_알람_목록, 알람_식별자값을_기준_내림차순으로_정렬한다());

        return 예상된_알람_목록;
    }

    private Alarm 러너_게시글_작성자에게_알람을_저장한다(final Long 생성된_러너_게시글_식별자값) {
        final Member 헤나_사용자 = memberRepository.getAsRunnerByRunnerPostId(생성된_러너_게시글_식별자값);

        final Alarm 저장되지_않은_알람 = Alarm.builder()
                .alarmTitle(new AlarmTitle("알람 테스트용 제목"))
                .alarmMessage(new AlarmMessage("알람 테스트용 내용"))
                .alarmType(AlarmType.RUNNER_POST)
                .alarmReferencedId(new AlarmReferencedId(1L))
                .isRead(new IsRead(false))
                .member(헤나_사용자)
                .build();

        return alarmRepository.save(저장되지_않은_알람);
    }

    private Comparator<Alarm> 알람_식별자값을_기준_내림차순으로_정렬한다() {
        return (left, right) -> left.getId() < right.getId() ? 1 : -1;
    }
}
