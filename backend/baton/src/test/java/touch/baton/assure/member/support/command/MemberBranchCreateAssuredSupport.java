package touch.baton.assure.member.support.command;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.HttpStatusAndLocationHeader;
import touch.baton.domain.member.command.service.dto.GithubRepoNameRequest;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpHeaders.LOCATION;

@SuppressWarnings("NonAsciiCharacters")
public class MemberBranchCreateAssuredSupport {

    private MemberBranchCreateAssuredSupport() {
    }

    public static MemberBranchCreateBuilder 클라이언트_요청() {
        return new MemberBranchCreateBuilder();
    }

    public static class MemberBranchCreateBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public MemberBranchCreateBuilder 액세스_토큰으로_로그인_한다(final String 액세스_토큰) {
            accessToken = 액세스_토큰;
            return this;
        }

        public MemberBranchCreateBuilder 입력받은_레포에_사용자_github_계정명으로_된_브랜치를_생성한다(final GithubRepoNameRequest 레포_이름_요청) {
            response = AssuredSupport.post("/api/v1/branch", accessToken, 레포_이름_요청);
            return this;
        }

        public MemberBranchCreateResponseBuilder 서버_응답() {
            return new MemberBranchCreateResponseBuilder(response);
        }
    }

    public static class MemberBranchCreateResponseBuilder {

        private final ExtractableResponse<Response> response;

        public MemberBranchCreateResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public void 레포에_브랜치_등록_성공을_검증한다(final HttpStatusAndLocationHeader 예상_성공_응답) {
            assertSoftly(softly -> {
                        softly.assertThat(response.statusCode()).isEqualTo(예상_성공_응답.getHttpStatus().value());
                        softly.assertThat(response.header(LOCATION)).contains(예상_성공_응답.getLocation());
                    }
            );
        }
    }
}
