package touch.baton.assure.tag;

import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.MockAuthCodes;
import touch.baton.domain.tag.command.Tag;
import touch.baton.domain.tag.command.vo.TagReducedName;

import java.time.LocalDateTime;
import java.util.List;

import static touch.baton.assure.runnerpost.support.command.RunnerPostCreateSupport.러너_게시글_생성_요청;
import static touch.baton.assure.runnerpost.support.command.RunnerPostCreateSupport.클라이언트_요청;
import static touch.baton.assure.tag.TagAssuredSupport.태그_검색_Detail_응답;

@SuppressWarnings("NonAsciiCharacters")
class TagReadAssuredTest extends AssuredTestConfig {

    @Test
    void 태그_검색에_성공한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());
        러너_게시글_생성을_성공한다(헤나_액세스_토큰, List.of("java", "javascript", "script"));
        final TagReducedName reducedName = TagReducedName.from("ja");
        final List<Tag> 검색된_태그_목록 = tagRepository.readTagsByReducedName(reducedName);

        // when, then
        TagAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .태그_이름을_오름차순으로_10개_검색한다("ja")

                .서버_응답()
                .태그_검색_성공을_검증한다(
                        태그_검색_Detail_응답(검색된_태그_목록)
                );
    }

    public static void 러너_게시글_생성을_성공한다(final String 사용자_액세스_토큰, final List<String> 태그_목록) {
        클라이언트_요청()
                .액세스_토큰으로_로그인한다(사용자_액세스_토큰)
                .러너_게시글_등록_요청한다(
                        러너_게시글_생성_요청(
                                "테스트용_러너_게시글_제목",
                                태그_목록,
                                "https://test-pull-request.com",
                                LocalDateTime.now().plusHours(100),
                                "테스트용_러너_게시글_구현_내용",
                                "테스트용_러너_게시글_궁금한_내용",
                                "테스트용_러너_게시글_참고_사항"
                        )
                )

                .서버_응답()
                .러너_게시글_생성_성공을_검증한다();
    }
}
