package touch.baton.assure.tag.query;

import org.junit.jupiter.api.Test;
import touch.baton.assure.runnerpost.support.command.RunnerPostCreateSupport;
import touch.baton.assure.tag.support.query.TagQuerySupport;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.FakeAuthCodes;
import touch.baton.domain.tag.command.Tag;
import touch.baton.domain.tag.command.vo.TagReducedName;
import touch.baton.domain.tag.query.controller.response.TagSearchResponses;

import java.time.LocalDateTime;
import java.util.List;

import static touch.baton.assure.runnerpost.support.command.RunnerPostCreateSupport.러너_게시글_생성_요청;


@SuppressWarnings("NonAsciiCharacters")
class TagReadAssuredTest extends AssuredTestConfig {

    @Test
    void 입력된_문자열로_태그_목록_검색에_성공한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(FakeAuthCodes.hyenaAuthCode());
        러너_게시글과_태그를_생성한다(헤나_액세스_토큰, List.of("java", "javascript", "script"));

        final TagReducedName 요청할_태그_이름 = TagReducedName.nullableInstance("ja");

        // when, then
        final List<Tag> 검색된_태그_목록 = tagQueryRepository.findTagsByReducedName(요청할_태그_이름);

        TagQuerySupport
                .클라이언트_요청()
                .입력된_문자열로_태그_목록을_검색한다(요청할_태그_이름)

                .서버_응답()
                .입력된_문자열로_태그_목록_검색_성공을_검증한다(
                        TagSearchResponses.Detail.from(검색된_태그_목록)
                );
    }

    private void 러너_게시글과_태그를_생성한다(final String 액세스_토큰, final List<String> 태그_목록) {
        RunnerPostCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(액세스_토큰)
                .러너_게시글_등록_요청한다(러너_게시글_생성_요청(
                        "테스트용_러너_게시글_제목",
                        태그_목록,
                        "https://test-pull-request.com",
                        LocalDateTime.now().plusHours(100),
                        "테스트용_러너_게시글_구현_내용",
                        "테스트용_러너_게시글_궁금한_내용",
                        "테스트용_러너_게시글_참고_사항"
                ))

                .서버_응답()
                .러너_게시글_생성_성공을_검증한다();
    }
}
