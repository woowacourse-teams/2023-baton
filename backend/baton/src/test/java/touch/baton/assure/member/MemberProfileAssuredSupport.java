package touch.baton.assure.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.controller.response.LoginMemberInfoResponse;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
public class MemberProfileAssuredSupport {

    private MemberProfileAssuredSupport() {
    }

    public static MemberClientRequestBuilder 클라이언트_요청() {
        return new MemberClientRequestBuilder();
    }

    public static LoginMemberInfoResponse 로그인_한_맴버_프로필_응답(final Member 맴버) {
        return LoginMemberInfoResponse.from(맴버);
    }
    public static class MemberClientRequestBuilder {

        private ExtractableResponse<Response> response;

        public MemberClientRequestBuilder 로그인_유저_프로필을_액세스_토큰으로_조회한다(final String accessToken) {
            response = AssuredSupport.get("api/v1/profile/me", accessToken);
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

        public void 맴버_로그인_프로필_조회_성공을_검증한다(final LoginMemberInfoResponse 맴버_로그인_프로필_응답) {
            final LoginMemberInfoResponse actual = this.response.as(LoginMemberInfoResponse.class);

            assertSoftly(softly -> {
                softly.assertThat(actual.name()).isEqualTo(맴버_로그인_프로필_응답.name());
                softly.assertThat(actual.imageUrl()).isEqualTo(맴버_로그인_프로필_응답.imageUrl());
            });
        }
    }
}
