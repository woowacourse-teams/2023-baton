package touch.baton.assure.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.HttpStatusAndLocationHeader;
import touch.baton.domain.member.service.dto.GithubRepoNameRequest;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpHeaders.LOCATION;

@SuppressWarnings("NonAsciiCharacters")
class MemberBranchAssuredSupport {

    private MemberBranchAssuredSupport() {
    }

    public static MemberClientRequestBuilder 클라이언트_요청() {
        return new MemberClientRequestBuilder();
    }

    public static class MemberClientRequestBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public MemberClientRequestBuilder 액세스_토큰으로_로그인_한다(final String 액세스_토큰) {
            accessToken = 액세스_토큰;
            return this;
        }

        public MemberClientRequestBuilder 사용자_본인_프로필을_가지고_있는_액세스_토큰으로_조회한다() {
            response = AssuredSupport.get("api/v1/profile/me", accessToken);
            return this;
        }

        public MemberClientRequestBuilder 입력받은_레포에_사용자_github_계정명으로_된_브랜치를_생성한다(final GithubRepoNameRequest 레포_이름_요청) {
            response = AssuredSupport.post("/api/v1/branch", accessToken, 레포_이름_요청);
            return this;
        }

        public MemberServerResponseBuilder 서버_응답() {
            return new MemberServerResponseBuilder(response);
        }
    }

    public static class MemberServerResponseBuilder {

        private final ExtractableResponse<Response> response;

        public MemberServerResponseBuilder(final ExtractableResponse<Response> response) {
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
