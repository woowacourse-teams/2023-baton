package touch.baton.assure.runnerpost;

import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.MockAuthCodes;
import touch.baton.tobe.domain.member.command.Supporter;
import touch.baton.tobe.domain.member.command.vo.SocialId;
import touch.baton.tobe.domain.runnerpost.command.controller.response.SupporterRunnerPostResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static touch.baton.assure.runnerpost.RunnerPostAssuredCreateSupport.러너_게시글_생성_요청;
import static touch.baton.assure.runnerpost.RunnerPostAssuredSupport.지원한_서포터_응답;
import static touch.baton.assure.runnerpost.RunnerPostAssuredSupport.지원한_서포터_응답_목록_응답;

@SuppressWarnings("NonAsciiCharacters")
class RunnerPostReadWithLoginedAssuredTest extends AssuredTestConfig {

    @Test
    void 러너의_게시글_식별자값으로_지원한_서포터_목록_조회에_성공한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());
        final String 디투_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.ditooAuthCode());
        final Long 디투_러너_게시글_식별자값 = 러너_게시글_생성을_성공하고_러너_게시글_식별자값을_반환한다(디투_액세스_토큰);

        // when
        서포터가_러너_게시글에_리뷰_신청을_성공한다(헤나_액세스_토큰, 디투_러너_게시글_식별자값);

        final SocialId 헤나_소셜_아이디 = jwtTestManager.parseToSocialId(헤나_액세스_토큰);
        final Supporter 서포터_헤나 = supporterRepository.getBySocialId(헤나_소셜_아이디);

        final SupporterRunnerPostResponse.Detail 지원한_서포터_헤나_응답 = 지원한_서포터_응답(
                서포터_헤나,
                0,
                "안녕하세요. 서포터 헤나입니다.",
                Collections.emptyList()
        );

        // then
        RunnerPostAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(디투_액세스_토큰)
                .러너_게시글_식별자값으로_지원한_서포터_목록을_조회한다(디투_러너_게시글_식별자값)

                .서버_응답()
                .지원한_서포터_목록_조회_성공을_검증한다(지원한_서포터_응답_목록_응답(List.of(지원한_서포터_헤나_응답)));
    }

    private Long 러너_게시글_생성을_성공하고_러너_게시글_식별자값을_반환한다(final String 헤나_액세스_토큰) {
        return RunnerPostAssuredCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너가_러너_게시글을_작성한다(
                        러너_게시글_생성_요청(
                                "테스트용_러너_게시글_제목",
                                List.of("자바", "스프링"),
                                "https://test-pull-request.com",
                                LocalDateTime.now().plusHours(100),
                                "테스트용_러너_게시글_구현_내용",
                                "테스트용_러너_게시글_궁금한_내용",
                                "테스트용_러너_게시글_참고_사항"
                        )
                )

                .서버_응답()
                .러너_게시글_생성_성공을_검증한다()
                .생성한_러너_게시글의_식별자값을_반환한다();
    }

    private void 서포터가_러너_게시글에_리뷰_신청을_성공한다(final String 서포터_액세스_토큰, final Long 러너_게시글_식별자값) {
        RunnerPostAssuredCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(서포터_액세스_토큰)
                .서포터가_러너_게시글에_리뷰를_신청한다(러너_게시글_식별자값, "안녕하세요. 서포터 헤나입니다.")

                .서버_응답()
                .서포터가_러너_게시글에_리뷰_신청_성공을_검증한다(러너_게시글_식별자값);
    }
}
