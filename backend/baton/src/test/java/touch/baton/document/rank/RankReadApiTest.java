package touch.baton.document.rank;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.member.query.controller.response.RankResponses;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RankReadApiTest extends RestdocsConfig {

    @DisplayName("완료된 리뷰가 많은 서포터 랭킹 조회 API")
    @Test
    void readMostReviewSupporter() throws Exception {
        // given
        final List<RankResponses.SupporterResponse> response = List.of(
                new RankResponses.SupporterResponse(1, "ethan", 1L, 10, "imageUrl", "githubUrl", "우아한테크코스", List.of("java", "spring")),
                new RankResponses.SupporterResponse(2, "hyena", 2L, 9, "imageUrl", "githubUrl", "우아한테크코스", List.of("java", "spring")),
                new RankResponses.SupporterResponse(3, "ditoo", 3L, 8, "imageUrl", "githubUrl", "우아한테크코스", List.of("java", "spring")),
                new RankResponses.SupporterResponse(4, "aiden", 4L, 7, "imageUrl", "githubUrl", "우아한테크코스", List.of("javascript", "react")),
                new RankResponses.SupporterResponse(5, "ditoo", 5L, 6, "imageUrl", "githubUrl", "우아한테크코스", List.of("javascript", "react"))
        );

        // when
        when(rankQueryService.readMostReviewSupporter(5)).thenReturn(new RankResponses(response));

        // then
        mockMvc.perform(get("/api/v1/rank/supporter")
                        .queryParam("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("limit").description("조회할 랭킹 개수")
                        ),
                        responseFields(
                                fieldWithPath("data.[].rank").type(NUMBER).description("서포터 랭킹"),
                                fieldWithPath("data.[].name").type(STRING).description("서포터 이름"),
                                fieldWithPath("data.[].supporterId").type(NUMBER).description("서포터 아이디"),
                                fieldWithPath("data.[].reviewedCount").type(NUMBER).description("서포터가 완료한 리뷰 수"),
                                fieldWithPath("data.[].imageUrl").type(STRING).description("서포터 프로필 이미지 URL"),
                                fieldWithPath("data.[].githubUrl").type(STRING).description("서포터 깃허브 URL"),
                                fieldWithPath("data.[].company").type(STRING).description("서포터 소속"),
                                fieldWithPath("data.[].technicalTags").type(ARRAY).description("서포터 기술 태그 목록")
                        )
                ));
    }
}
