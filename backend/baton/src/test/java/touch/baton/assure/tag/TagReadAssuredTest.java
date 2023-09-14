package touch.baton.assure.tag;

import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.MockAuthCodes;
import touch.baton.domain.tag.Tag;

import java.util.List;

import static touch.baton.assure.tag.TagAssuredSupport.러너_게시글_생성을_성공한다;
import static touch.baton.assure.tag.TagAssuredSupport.태그_검색_Detail_응답;

@SuppressWarnings("NonAsciiCharacters")
class TagReadAssuredTest extends AssuredTestConfig {

    @Test
    void 태그_검색에_성공한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());
        러너_게시글_생성을_성공한다(헤나_액세스_토큰, List.of("java", "javascript", "script"));
        final List<Tag> 검색된_태그_목록 = tagRepository.readTagsByReducedName("ja");

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
}
