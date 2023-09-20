package touch.baton.assure.member;

import org.junit.jupiter.api.Test;
import touch.baton.assure.common.HttpStatusAndLocationHeader;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.MockAuthCodes;
import touch.baton.domain.member.service.dto.GithubRepoNameRequest;

import static org.springframework.http.HttpStatus.CREATED;

@SuppressWarnings("NonAsciiCharacters")
class MemberBranchCreateAssuredTest extends AssuredTestConfig {

    @Test
    void 로그인_한_사용자가_요청한_레포의_브랜치를_생성한다() {
        final String 디투_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.ditooAuthCode());
        final GithubRepoNameRequest 브랜치_생성_요청 = new GithubRepoNameRequest("ditoo");

        MemberBranchAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인_한다(디투_액세스_토큰)
                .입력받은_레포에_사용자_github_계정명으로_된_브랜치를_생성한다(브랜치_생성_요청)

                .서버_응답()
                .레포에_브랜치_등록_성공을_검증한다(new HttpStatusAndLocationHeader(CREATED, "/api/v1/profile/me"));
    }
}
