package touch.baton.document.profile.supporter.read;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.command.vo.SocialId;
import touch.baton.domain.technicaltag.command.TechnicalTag;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.TechnicalTagFixture;

import java.util.List;
import java.util.Optional;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.spy;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static touch.baton.fixture.vo.ReviewCountFixture.reviewCount;
import static touch.baton.fixture.vo.TagNameFixture.tagName;

class SupporterReadByGuestApiTest extends RestdocsConfig {

    @DisplayName("서포터 프로필 조회 API")
    @Test
    void readProfileBySupporterId() throws Exception {
        // given
        final TechnicalTag java = TechnicalTagFixture.create(tagName("java"));
        final TechnicalTag spring = TechnicalTagFixture.create(tagName("spring"));
        final Supporter supporter = SupporterFixture.create(reviewCount(0), MemberFixture.createHyena(), List.of(java, spring));
        final Supporter spySupporter = spy(supporter);

        when(spySupporter.getId()).thenReturn(1L);
        when(supporterQueryService.readBySupporterId(spySupporter.getId())).thenReturn(spySupporter);

        // then
        mockMvc.perform(get("/api/v1/profile/supporter/{supporterId}", 1L))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("supporterId").description("서포터 식별자값")
                        ),
                        responseFields(
                                fieldWithPath("supporterId").type(NUMBER).description("서포터 식별자값"),
                                fieldWithPath("name").type(STRING).description("서포터 이름"),
                                fieldWithPath("company").type(STRING).description("서포터 소속 회사"),
                                fieldWithPath("imageUrl").type(STRING).description("서포터 프로필 이미지 url"),
                                fieldWithPath("githubUrl").type(STRING).description("서포터 깃허브 url"),
                                fieldWithPath("introduction").type(STRING).description("서포터 자기소개"),
                                fieldWithPath("technicalTags").type(ARRAY).description("서포터 기술 태그 목록")
                        )
                ));
    }

    @DisplayName("서포터 마이페이지 프로필 조회 API")
    @Test
    void readMyProfileByToken() throws Exception {
        // given
        final String socialId = "hello ditoo";
        final Member ditooMember = MemberFixture.createWithSocialId(socialId);
        final TechnicalTag javaTag = TechnicalTagFixture.createJava();
        final TechnicalTag reactTag = TechnicalTagFixture.createReact();
        final Supporter supporter = SupporterFixture.create(ditooMember, List.of(javaTag, reactTag));
        final Supporter spySupporter = spy(supporter);
        final String accessToken = getAccessTokenBySocialId(socialId);

        // when
        when(spySupporter.getId()).thenReturn(1L);
        when(oauthSupporterCommandRepository.joinByMemberSocialId(any(SocialId.class))).thenReturn(Optional.ofNullable(spySupporter));

        // then
        mockMvc.perform(get("/api/v1/profile/supporter/me").header(AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(headerWithName(AUTHORIZATION).description("Bearer JWT")),
                        responseFields(
                                fieldWithPath("name").type(STRING).description("서포터 이름"),
                                fieldWithPath("imageUrl").type(STRING).description("서포터 프로필 이미지 url"),
                                fieldWithPath("githubUrl").type(STRING).description("서포터 깃허브 url"),
                                fieldWithPath("introduction").type(STRING).description("서포터 자기소개"),
                                fieldWithPath("company").type(STRING).description("서포터 소속 회사"),
                                fieldWithPath("technicalTags").type(ARRAY).description("서포터 기술 태그 목록")
                        )
                ));
    }
}
