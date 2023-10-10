package touch.baton.document.oauth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.member.command.Member;
import touch.baton.fixture.domain.MemberFixture;

import java.util.Optional;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OauthLogoutApiTest extends RestdocsConfig {

    @DisplayName("로그아웃을 하면 리프레시 토큰이 삭제된다.")
    @Test
    void logout() throws Exception {
        // given & when
        final Member ethan = MemberFixture.createEthan();
        final String accessToken = getAccessTokenBySocialId(ethan.getSocialId().getValue());
        given(oauthMemberCommandRepository.findBySocialId(any())).willReturn(Optional.ofNullable(ethan));

        // then
        mockMvc.perform(delete("/api/v1/oauth/logout")
                        .header(AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isNoContent())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("Access Token")
                        )
                ))
                .andDo(print());
    }
}
