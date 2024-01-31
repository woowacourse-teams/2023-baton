package touch.baton.document.oauth.token;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.oauth.command.AuthorizationHeader;
import touch.baton.domain.oauth.command.token.AccessToken;
import touch.baton.domain.oauth.command.token.RefreshToken;
import touch.baton.domain.oauth.command.token.Token;
import touch.baton.domain.oauth.command.token.Tokens;
import touch.baton.fixture.domain.MemberFixture;

import java.time.Duration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.cookies.CookieDocumentation.responseCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RefreshTokenApiTest extends RestdocsConfig {

    @DisplayName("만료된 jwt 토큰과 refresh token 으로 refresh 요청을 하면 새로운 토큰들이 반환된다.")
    @Test
    void refresh() throws Exception {
        // given, when
        final RefreshToken refreshToken = RefreshToken.builder()
                .socialId("mock socialId")
                .token(new Token("refresh-token"))
                .member(MemberFixture.createEthan())
                .timeout(30L)
                .build();
        final Tokens tokens = new Tokens(new AccessToken("renew access token"), refreshToken);
        final Cookie cookie = createCookie();

        given(oauthCommandService.reissueAccessToken(any(AuthorizationHeader.class), any(Token.class))).willReturn(tokens);

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
                ));
    }

    private Cookie createCookie() {
        final Cookie cookie = new Cookie("refreshToken", "refresh-token");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) Duration.ofDays(30).toSeconds());
        return cookie;
    }
}
