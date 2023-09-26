package touch.baton.document.oauth.token;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import touch.baton.config.RestdocsConfig;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.tobe.domain.oauth.command.AuthorizationHeader;
import touch.baton.tobe.domain.oauth.command.controller.OauthCommandController;
import touch.baton.tobe.domain.oauth.command.service.OauthCommandService;
import touch.baton.tobe.domain.oauth.command.token.AccessToken;
import touch.baton.tobe.domain.oauth.command.token.ExpireDate;
import touch.baton.tobe.domain.oauth.command.token.RefreshToken;
import touch.baton.tobe.domain.oauth.command.token.Token;
import touch.baton.tobe.domain.oauth.command.token.Tokens;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.cookies.CookieDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OauthCommandController.class)
class RefreshTokenApiTest extends RestdocsConfig {

    @MockBean
    private OauthCommandService oauthCommandService;

    @BeforeEach
    void setUp() {
        final OauthCommandController oauthCommandController = new OauthCommandController(oauthCommandService);
        restdocsSetUp(oauthCommandController);
    }

    @DisplayName("만료된 jwt 토큰과 refresh token 으로 refresh 요청을 하면 새로운 토큰들이 반환된다.")
    @Test
    void refresh() throws Exception {
        // given & when
        final RefreshToken refreshToken = RefreshToken.builder()
                .token(new Token("refresh-token"))
                .member(MemberFixture.createEthan())
                .expireDate(new ExpireDate(LocalDateTime.now().plusDays(30)))
                .build();
        final Tokens tokens = new Tokens(new AccessToken("renew access token"), refreshToken);
        final Cookie cookie = createCookie();

        given(oauthCommandService.reissueAccessToken(any(AuthorizationHeader.class), any(String.class))).willReturn(tokens);

        // then
        mockMvc.perform(post("/api/v1/oauth/refresh")
                        .header(AUTHORIZATION, "expired access token")
                        .cookie(cookie))
                .andExpect(status().isNoContent())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("만료된 JWT 토큰")
                        ),
                        requestCookies(
                                cookieWithName("refreshToken").description("만료되지 않은 리프레시 토큰")
                        ),
                        responseHeaders(
                                headerWithName("Authorization").description("새로 발급된 JWT 토큰")
                        ),
                        responseCookies(
                                cookieWithName("refreshToken").description("새로 발급된 리프레시 토큰")
                        )
                ))
                .andDo(print());
    }

    private Cookie createCookie() {
        final Cookie cookie = new Cookie("refreshToken", "refresh-token");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) Duration.ofDays(30).toSeconds());
        return cookie;
    }
}
