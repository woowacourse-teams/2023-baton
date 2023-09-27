package touch.baton.assure.member.support.command;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.HttpStatusAndLocationHeader;
import touch.baton.assure.common.PathParams;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.response.ErrorResponse;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.service.dto.RunnerUpdateRequest;
import touch.baton.domain.member.query.controller.response.RunnerProfileResponse;
import touch.baton.domain.member.query.controller.response.RunnerResponse;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
public class RunnerUpdateAssuredSupport {

    private RunnerUpdateAssuredSupport() {
    }

    public static RunnerUpdateBuilder 클라이언트_요청() {
        return new RunnerUpdateBuilder();
    }

    public static RunnerResponse.MyProfile 러너_본인_프로필_응답(final Runner 러너, final List<String> 러너_태그_목록) {
        return new RunnerResponse.MyProfile(
                러너.getMember().getMemberName().getValue(),
                러너.getMember().getCompany().getValue(),
                러너.getMember().getImageUrl().getValue(),
                러너.getMember().getGithubUrl().getValue(),
                러너.getIntroduction().getValue(),
                러너_태그_목록
        );
    }

    public static RunnerUpdateRequest 러너_본인_프로필_수정_요청(final String 러너_이름,
                                                      final String 회사,
                                                      final String 러너_소개글,
                                                      final List<String> 러너_기술태그_목록
    ) {
        return new RunnerUpdateRequest(러너_이름, 회사, 러너_소개글, 러너_기술태그_목록);
    }

    public static class RunnerUpdateBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public RunnerUpdateBuilder 액세스_토큰으로_로그인한다(final String 액세스_토큰) {
            this.accessToken = 액세스_토큰;
            return this;
        }

        public RunnerUpdateBuilder 러너_본인_프로필을_가지고_있는_액세스_토큰으로_조회한다() {
            response = AssuredSupport.get("/api/v1/profile/runner/me", accessToken);
            return this;
        }

        public RunnerUpdateBuilder 러너_프로필을_상세_조회한다(final Long 러너_식별자) {
            response = AssuredSupport.get("/api/v1/profile/runner/{runnerId}", new PathParams(Map.of("runnerId", 러너_식별자)));
            return this;
        }

        public RunnerUpdateBuilder 러너_본인_프로필을_수정한다(final RunnerUpdateRequest 러너_업데이트_요청) {
            response = AssuredSupport.patch("/api/v1/profile/runner/me", accessToken, 러너_업데이트_요청);
            return this;
        }

        public RunnerUpdateResponseBuilder 서버_응답() {
            return new RunnerUpdateResponseBuilder(response);
        }
    }

    public static class RunnerUpdateResponseBuilder {

        private final ExtractableResponse<Response> response;

        public RunnerUpdateResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public static RunnerUpdateBuilder 클라이언트_요청() {
            return new RunnerUpdateBuilder();
        }

        public void 러너_본인_프로필_조회_성공을_검증한다(final RunnerResponse.MyProfile 러너_본인_프로필_응답) {
            final RunnerResponse.MyProfile actual = this.response.as(RunnerResponse.MyProfile.class);

            assertSoftly(softly -> {
                softly.assertThat(actual.name()).isEqualTo(러너_본인_프로필_응답.name());
                softly.assertThat(actual.company()).isEqualTo(러너_본인_프로필_응답.company());
                softly.assertThat(actual.imageUrl()).isEqualTo(러너_본인_프로필_응답.imageUrl());
                softly.assertThat(actual.githubUrl()).isEqualTo(러너_본인_프로필_응답.githubUrl());
                softly.assertThat(actual.introduction()).isEqualTo(러너_본인_프로필_응답.introduction());
                softly.assertThat(actual.technicalTags()).isEqualTo(러너_본인_프로필_응답.technicalTags());
            });
        }

        public void 러너_프로필_상세_조회를_검증한다(final RunnerProfileResponse.Detail 러너_프로필_상세_응답) {
            final RunnerProfileResponse.Detail actual = this.response.as(RunnerProfileResponse.Detail.class);

            assertSoftly(softly -> {
                        softly.assertThat(actual.runnerId()).isNotNull();
                        softly.assertThat(actual.name()).isEqualTo(러너_프로필_상세_응답.name());
                        softly.assertThat(actual.imageUrl()).isEqualTo(러너_프로필_상세_응답.imageUrl());
                        softly.assertThat(actual.githubUrl()).isEqualTo(러너_프로필_상세_응답.githubUrl());
                        softly.assertThat(actual.introduction()).isEqualTo(러너_프로필_상세_응답.introduction());
                        softly.assertThat(actual.company()).isEqualTo(러너_프로필_상세_응답.company());
                        softly.assertThat(actual.technicalTags()).containsExactlyElementsOf(러너_프로필_상세_응답.technicalTags());
                    }
            );
        }

        public void 러너_본인_프로필_수정_성공을_검증한다(final HttpStatusAndLocationHeader 응답상태_및_로케이션) {
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(응답상태_및_로케이션.getHttpStatus().value());
                softly.assertThat(response.header(HttpHeaders.LOCATION)).contains(응답상태_및_로케이션.getLocation());
            });
        }

        public void 러너_본인_프로필_수정_실패를_검증한다(final ClientErrorCode 클라이언트_에러_코드) {
            final ErrorResponse actual = this.response.as(ErrorResponse.class);

            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(클라이언트_에러_코드.getHttpStatus().value());
                softly.assertThat(actual.errorCode()).isEqualTo(클라이언트_에러_코드.getErrorCode());
            });
        }
    }
}
