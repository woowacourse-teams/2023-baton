package touch.baton.document.profile.supporter.update;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.controller.SupporterProfileController;
import touch.baton.domain.supporter.service.SupporterService;
import touch.baton.domain.supporter.service.dto.SupporterUpdateRequest;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.SupporterFixture;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static touch.baton.fixture.vo.CompanyFixture.company;
import static touch.baton.fixture.vo.GithubUrlFixture.githubUrl;
import static touch.baton.fixture.vo.ImageUrlFixture.imageUrl;
import static touch.baton.fixture.vo.MemberNameFixture.memberName;
import static touch.baton.fixture.vo.OauthIdFixture.oauthId;
import static touch.baton.fixture.vo.SocialIdFixture.socialId;

@WebMvcTest(SupporterProfileController.class)
public class SupporterProfileUpdateApiTest extends RestdocsConfig {

    @MockBean
    private SupporterService supporterService;

    @BeforeEach
    void setUp() {
        final SupporterProfileController supporterProfileController = new SupporterProfileController(supporterService);
        restdocsSetUp(supporterProfileController);
    }

    @DisplayName("서포터 프로필 수정 API")
    @Test
    void updateSupporterProfile() throws Exception {
        // given
        final SupporterUpdateRequest request = new SupporterUpdateRequest("디투랜드", "우아한테크코스", "안녕하세요. 디투입니다.", List.of("java", "python"));
        final String requestBody = objectMapper.writeValueAsString(request);
        final String socialId = "ditooSocialId";
        final Member member = MemberFixture.create(
                memberName("디투"),
                socialId(socialId),
                oauthId("abcd"),
                githubUrl("naver.com"),
                company("우아한테크코스"),
                imageUrl("profile.jpg")
        );
        final Supporter supporter = SupporterFixture.create(member);
        final String token = getAccessTokenBySocialId(socialId);

        // when
        when(oauthSupporterRepository.joinByMemberSocialId(any()))
                .thenReturn(Optional.ofNullable(supporter));

        // then
        mockMvc.perform(patch("/api/v1/profile/supporter/me")
                        .header(AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isNoContent())
                .andExpect(header().string("Location", "/api/v1/profile/supporter/me"))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Bearer JWT"),
                                headerWithName(CONTENT_TYPE).description("application/json")
                        ),
                        requestFields(
                                fieldWithPath("name").type(STRING).description("변경할 이름"),
                                fieldWithPath("company").type(STRING).description("변경할 소속"),
                                fieldWithPath("introduction").type(STRING).description("변경할 소개글"),
                                fieldWithPath("technicalTags.[]").type(ARRAY).description("변경할 기술 태그 목록")
                        ),
                        responseHeaders(headerWithName(LOCATION).description("redirect uri"))
                ))
                .andDo(print());
    }
}
