package touch.baton.assure.member.support.query;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.PathParams;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.command.service.dto.SupporterUpdateRequest;
import touch.baton.domain.member.query.controller.response.SupporterResponse;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
public class SupporterQueryAssuredSupport {

    private SupporterQueryAssuredSupport() {
    }

    public static SupporterQueryBuilder 클라이언트_요청() {
        return new SupporterQueryBuilder();
    }

    public static SupporterResponse.Profile 서포터_Profile_응답(final Supporter 서포터, final List<String> 서포터_태그_목록) {
        return new SupporterResponse.Profile(
                서포터.getId(),
                서포터.getMember().getMemberName().getValue(),
                서포터.getMember().getCompany().getValue(),
                서포터.getMember().getImageUrl().getValue(),
                서포터.getMember().getGithubUrl().getValue(),
                서포터.getIntroduction().getValue(),
                서포터_태그_목록
        );
    }

    public static SupporterResponse.MyProfile 서포터_MyProfile_응답(final Supporter 서포터, final List<String> 서포터_태그_목록) {
        return new SupporterResponse.MyProfile(
                서포터.getMember().getMemberName().getValue(),
                서포터.getMember().getImageUrl().getValue(),
                서포터.getMember().getGithubUrl().getValue(),
                서포터.getIntroduction().getValue(),
                서포터.getMember().getCompany().getValue(),
                서포터_태그_목록
        );
    }

    public static class SupporterQueryBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public SupporterQueryBuilder 액세스_토큰으로_로그인_한다(final String 액세스_토큰) {
            accessToken = 액세스_토큰;
            return this;
        }

        public SupporterQueryBuilder 서포터_프로필을_서포터_식별자값으로_조회한다(final Long 서포터_식별자값) {
            response = AssuredSupport.get("/api/v1/profile/supporter/{supporterId}", new PathParams(Map.of("supporterId", 서포터_식별자값)));
            return this;
        }

        public SupporterQueryBuilder 서포터_본인_프로필을_수정한다(final SupporterUpdateRequest 서포터_업데이트_요청) {
            response = AssuredSupport.patch("/api/v1/profile/supporter/me", accessToken, 서포터_업데이트_요청);
            return this;
        }

        public SupporterQueryBuilder 서포터_마이페이지를_액세스_토큰으로_조회한다() {
            response = AssuredSupport.get("/api/v1/profile/supporter/me", accessToken);
            return this;
        }

        public SupporterQueryResponseBuilder 서버_응답() {
            return new SupporterQueryResponseBuilder(response);
        }
    }

    public static class SupporterQueryResponseBuilder {

        private final ExtractableResponse<Response> response;

        public SupporterQueryResponseBuilder(final ExtractableResponse<Response> 응답) {
            this.response = 응답;
        }

        public void 서포터_프로필_조회_성공을_검증한다(final SupporterResponse.Profile 서포터_프로필_응답) {
            final SupporterResponse.Profile actual = this.response.as(SupporterResponse.Profile.class);

            assertSoftly(softly -> {
                        softly.assertThat(actual.supporterId()).isEqualTo(서포터_프로필_응답.supporterId());
                        softly.assertThat(actual.name()).isEqualTo(서포터_프로필_응답.name());
                        softly.assertThat(actual.company()).isEqualTo(서포터_프로필_응답.company());
                        softly.assertThat(actual.imageUrl()).isEqualTo(서포터_프로필_응답.imageUrl());
                        softly.assertThat(actual.githubUrl()).isEqualTo(서포터_프로필_응답.githubUrl());
                        softly.assertThat(actual.introduction()).isEqualTo(서포터_프로필_응답.introduction());
                        softly.assertThat(actual.technicalTags()).isEqualTo(서포터_프로필_응답.technicalTags());
                    }
            );
        }

        public void 서포터_마이페이지_프로필_조회_성공을_검증한다(final SupporterResponse.MyProfile 서포터_마이페이지_프로필_응답) {
            final SupporterResponse.MyProfile actual = this.response.as(SupporterResponse.MyProfile.class);

            assertSoftly(softly -> {
                        softly.assertThat(actual.name()).isEqualTo(서포터_마이페이지_프로필_응답.name());
                        softly.assertThat(actual.company()).isEqualTo(서포터_마이페이지_프로필_응답.company());
                        softly.assertThat(actual.imageUrl()).isEqualTo(서포터_마이페이지_프로필_응답.imageUrl());
                        softly.assertThat(actual.githubUrl()).isEqualTo(서포터_마이페이지_프로필_응답.githubUrl());
                        softly.assertThat(actual.introduction()).isEqualTo(서포터_마이페이지_프로필_응답.introduction());
                        softly.assertThat(actual.technicalTags()).isEqualTo(서포터_마이페이지_프로필_응답.technicalTags());
                    }
            );
        }
    }
}
