package touch.baton.assure.runnerpost;

import org.springframework.http.HttpStatus;
import touch.baton.assure.common.HttpStatusAndLocationHeader;
import touch.baton.assure.runnerpost.support.command.RunnerPostCreateSupport;
import touch.baton.assure.runnerpost.support.command.RunnerPostUpdateSupport;
import touch.baton.assure.runnerpost.support.command.applicant.RunnerPostApplicantCreateSupport;
import touch.baton.domain.member.command.Supporter;

import java.time.LocalDateTime;
import java.util.List;

import static touch.baton.assure.runnerpost.support.command.RunnerPostCreateSupport.러너_게시글_생성_요청;
import static touch.baton.assure.runnerpost.support.command.applicant.RunnerPostApplicantCreateSupport.러너의_서포터_선택_요청;

public abstract class RunnerPostSteps {

    public static void 러너가_게시글을_작성하고_리뷰를_받은_뒤_리뷰완료로_변경한다(final String 러너_액세스_토큰, final String 서포터_액세스_토큰, final Supporter 서포터) {
        final Long 게시글_식별자 = 러너_게시글을_생성하고_게시을글_식별자를_반환한다(러너_액세스_토큰);
        서포터가_러너_게시글에_리뷰_신청을_성공한다(서포터_액세스_토큰, 게시글_식별자);
        러너가_서포터의_리뷰_신청_선택에_성공한다(서포터, 러너_액세스_토큰, 게시글_식별자);
        서포터가_러너_게시글의_리뷰를_완료로_변경하는_것을_성공한다(서포터_액세스_토큰, 게시글_식별자);
    }

    public static void 러너가_게시글을_작성하고_서포터가_선택된다(final String 러너_액세스_토큰, final String 서포터_액세스_토큰, final Supporter 서포터) {
        final Long 게시글_식별자 = 러너_게시글을_생성하고_게시을글_식별자를_반환한다(러너_액세스_토큰);
        서포터가_러너_게시글에_리뷰_신청을_성공한다(서포터_액세스_토큰, 게시글_식별자);
        러너가_서포터의_리뷰_신청_선택에_성공한다(서포터, 러너_액세스_토큰, 게시글_식별자);
    }

    public static Long 러너_게시글을_생성하고_게시을글_식별자를_반환한다(final String 러너_엑세스_토큰) {
        return RunnerPostCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(러너_엑세스_토큰)
                .러너_게시글_등록_요청한다(
                        러너_게시글_생성_요청(
                                "테스트용_러너_게시글_제목",
                                List.of("자바", "스프링"),
                                "https://test-pull-request.com",
                                LocalDateTime.now().plusHours(100),
                                "테스트용_러너_게시글_구현_내용",
                                "테스트용_러너_게시글_궁금한_내용",
                                "테스트용_러너_게시글_참고_사항"
                        ))

                .서버_응답()
                .러너_게시글_생성_성공을_검증한다()
                .생성한_러너_게시글의_식별자값을_반환한다();
    }

    public static void 서포터가_러너_게시글에_리뷰_신청을_성공한다(final String 서포터_액세스_토큰, final Long 러너_게시글_식별자값) {
        RunnerPostApplicantCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(서포터_액세스_토큰)
                .서포터가_러너_게시글에_리뷰를_신청한다(러너_게시글_식별자값, "안녕하세요. 서포터입니다.")

                .서버_응답()
                .서포터가_러너_게시글에_리뷰_신청_성공을_검증한다(러너_게시글_식별자값);
    }

    public static void 러너가_서포터의_리뷰_신청_선택에_성공한다(final Supporter 서포터, final String 러너_액세스_토큰, final Long 러너_게시글_식별자값) {
        RunnerPostUpdateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(러너_액세스_토큰)
                .러너가_서포터를_선택한다(러너_게시글_식별자값, 러너의_서포터_선택_요청(서포터.getId()))

                .서버_응답()
                .러너_게시글에_서포터가_성공적으로_선택되었는지_확인한다(new HttpStatusAndLocationHeader(HttpStatus.NO_CONTENT, "/api/v1/posts/runner"));
    }

    public static void 서포터가_러너_게시글의_리뷰를_완료로_변경하는_것을_성공한다(final String 서포터_액세스_토큰, final Long 러너_게시글_식별자값) {
        RunnerPostUpdateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(서포터_액세스_토큰)
                .서포터가_리뷰를_완료하고_리뷰완료_버튼을_누른다(러너_게시글_식별자값)

                .서버_응답()
                .러너_게시글이_성공적으로_리뷰_완료_상태인지_확인한다(new HttpStatusAndLocationHeader(HttpStatus.NO_CONTENT, "/api/v1/posts/runner"));
    }
}
