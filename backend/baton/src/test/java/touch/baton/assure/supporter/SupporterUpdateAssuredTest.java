package touch.baton.assure.supporter;

import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.MockAuthCodes;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static touch.baton.assure.supporter.SupporterAssuredSupport.서포터_본인_정보_수정_요청;
import static touch.baton.domain.common.exception.ClientErrorCode.COMPANY_IS_NULL;
import static touch.baton.domain.common.exception.ClientErrorCode.NAME_IS_NULL;
import static touch.baton.domain.common.exception.ClientErrorCode.SUPPORTER_INTRODUCTION_IS_NULL;
import static touch.baton.domain.common.exception.ClientErrorCode.SUPPORTER_TECHNICAL_TAGS_ARE_NULL;

@SuppressWarnings("NonAsciiCharacters")
class SupporterUpdateAssuredTest extends AssuredTestConfig {

    @Test
    void 서포터_정보를_수정한다() {
        // given
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        // when, then
        // when, then
        SupporterAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인_한다(헤나_액세스_토큰)
                .서포터_본인_프로필을_수정한다(
                        서포터_본인_정보_수정_요청("수정된_이름", "수정된_회사", "수정된_서포터_자기소개글", List.of("자바", "스프링"))
                )

                .서버_응답()
                .서포터_본인_프로필_수정_성공을_검증한다(NO_CONTENT);
    }

    @Test
    void 서포터_정보_수정_시에_이름이_없으면_예외가_발생한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        // when, then
        SupporterAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인_한다(헤나_액세스_토큰)
                .서포터_본인_프로필을_수정한다(
                        서포터_본인_정보_수정_요청(null, "수정된_회사", "수정된_서포터_자기소개글", List.of("자바", "스프링"))
                )

                .서버_응답()
                .서포터_본인_프로필_수정_실패를_검증한다(NAME_IS_NULL);
    }

    @Test
    void 서포터_정보_수정_시에_소속이_없으면_예외가_발생한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        // when, then
        SupporterAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인_한다(헤나_액세스_토큰)
                .서포터_본인_프로필을_수정한다(
                        서포터_본인_정보_수정_요청("수정된_이름", null, "수정된_서포터_자기소개글", List.of("자바", "스프링"))
                )

                .서버_응답()
                .서포터_본인_프로필_수정_실패를_검증한다(COMPANY_IS_NULL);
    }

    @Test
    void 서포터_정보_수정_시에_소개글이_없으면_예외가_발생한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        // when, then
        SupporterAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인_한다(헤나_액세스_토큰)
                .서포터_본인_프로필을_수정한다(
                        서포터_본인_정보_수정_요청("수정된_이름", "수정된_회사", null, List.of("자바", "스프링"))
                )

                .서버_응답()
                .서포터_본인_프로필_수정_실패를_검증한다(SUPPORTER_INTRODUCTION_IS_NULL);
    }

    @Test
    void 서포터_정보_수정_시에_기술_태그가_없으면_예외가_발생한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        // when, then
        SupporterAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인_한다(헤나_액세스_토큰)
                .서포터_본인_프로필을_수정한다(
                        서포터_본인_정보_수정_요청("수정된_이름", "수정된_회사", "수정된_서포터_자기소개글", null)
                )

                .서버_응답()
                .서포터_본인_프로필_수정_실패를_검증한다(SUPPORTER_TECHNICAL_TAGS_ARE_NULL);
    }
}
