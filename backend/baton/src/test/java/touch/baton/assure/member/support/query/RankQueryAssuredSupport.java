package touch.baton.assure.member.support.query;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.QueryParams;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.query.controller.response.RankResponses;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
public class RankQueryAssuredSupport {

    private RankQueryAssuredSupport() {
    }

    public static RankQueryBuilder 클라이언트_요청() {
        return new RankQueryBuilder();
    }

    public static class RankQueryBuilder {

        private ExtractableResponse<Response> response;

        public RankQueryBuilder 서포터_리뷰_랭킹을_조회한다(final int limit) {
            response = AssuredSupport.get("/api/v1/rank/supporter", new QueryParams(Map.of("limit", limit)));
            return this;
        }

        public RankQueryResponseBuilder 서버_응답() {
            return new RankQueryResponseBuilder(response);
        }
    }

    public static class RankQueryResponseBuilder {

        private final ExtractableResponse<Response> response;

        public RankQueryResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public void 서포터_리뷰_랭킹_조회_성공을_검증한다(final List<RankResponses.SupporterResponse> 랭킹_응답) {
            final RankResponses actual = this.response.as(RankResponses.class);

            assertSoftly(softly -> {
                softly.assertThat(actual.data()).hasSize(랭킹_응답.size());
                softly.assertThat(actual.data()).isEqualTo(랭킹_응답);
            });
        }

        public static RankResponses.SupporterResponse 서포터_리뷰_랭킹_응답(final int 순위, final Supporter 서포터, final int 리뷰수) {
            final Member member = 서포터.getMember();
            return new RankResponses.SupporterResponse(순위,
                    member.getMemberName().getValue(),
                    서포터.getId(),
                    리뷰수,
                    member.getImageUrl().getValue(),
                    member.getGithubUrl().getValue(),
                    member.getCompany().getValue(),
                    Collections.emptyList());
        }
    }
}
