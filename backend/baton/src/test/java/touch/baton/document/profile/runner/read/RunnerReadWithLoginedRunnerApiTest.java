package touch.baton.document.profile.runner.read;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.technicaltag.command.TechnicalTag;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.TechnicalTagFixture;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.BDDMockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static touch.baton.fixture.vo.TagNameFixture.tagName;

class RunnerReadWithLoginedRunnerApiTest extends RestdocsConfig {

    @DisplayName("러너 본인 프로필 조회 API")
    @Test
    void readMyProfileByToken() throws Exception {
        // given
        final TechnicalTag java = TechnicalTagFixture.create(tagName("java"));
        final TechnicalTag spring = TechnicalTagFixture.create(tagName("spring"));
        final Runner runner = RunnerFixture.createRunner(MemberFixture.createHyena(), List.of(java, spring));
        final String token = getAccessTokenBySocialId(runner.getMember().getSocialId().getValue());

        when(oauthRunnerCommandRepository.joinByMemberSocialId(notNull())).thenReturn(Optional.ofNullable(runner));

        // then
        mockMvc.perform(get("/api/v1/profile/runner/me")
                        .header(AUTHORIZATION, "Bearer " + token))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Bearer JWT")
                        ),
                        responseFields(
                                fieldWithPath("name").type(STRING).description("러너 이름"),
                                fieldWithPath("company").type(STRING).description("러너 소속 회사"),
                                fieldWithPath("imageUrl").type(STRING).description("러너 프로필 이미지 url"),
                                fieldWithPath("githubUrl").type(STRING).description("러너 깃허브 url"),
                                fieldWithPath("introduction").type(STRING).description("러너 자기소개"),
                                fieldWithPath("technicalTags").type(ARRAY).description("러너 기술 태그 목록")
                        )
                ));
    }
}
