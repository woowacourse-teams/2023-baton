package touch.baton.document.oauth.github;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import touch.baton.config.RestdocsConfig;
import touch.baton.tobe.domain.member.command.Member;
import touch.baton.domain.oauth.controller.OauthController;
import touch.baton.domain.oauth.service.OauthService;
import touch.baton.domain.oauth.token.AccessToken;
import touch.baton.domain.oauth.token.ExpireDate;
import touch.baton.domain.oauth.token.RefreshToken;
import touch.baton.domain.oauth.token.Token;
import touch.baton.domain.oauth.token.Tokens;
import touch.baton.infra.auth.oauth.github.GithubOauthConfig;

import java.time.LocalDateTime;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.mock;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.responseCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static touch.baton.domain.oauth.OauthType.GITHUB;

@EnableConfigurationProperties(GithubOauthConfig.class)
@TestPropertySource("classpath:application.yml")
@WebMvcTest(OauthController.class)
class GithubOauthApiTest extends RestdocsConfig {

    @MockBean
    private OauthService oauthService;

    @Autowired
    private GithubOauthConfig githubOauthConfig;

    @BeforeEach
    void setUp() {
        final OauthController oauthController = new OauthController(oauthService);
        restdocsSetUp(oauthController);
    }

    @DisplayName("Github 소셜 로그인을 위한 AuthCode 를 받을 수 있도록 사용자를 redirect 한다.")
    @Test
    void github_redirect_auth_code() throws Exception {
        // given & when
        when(oauthService.readAuthCodeRedirect(GITHUB))
                .thenReturn(githubOauthConfig.redirectUri());

        // then
        mockMvc.perform(get("/api/v1/oauth/{oauthType}", "github"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(githubOauthConfig.redirectUri()))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("oauthType").description("소셜 로그인 타입")
                        ),
                        responseHeaders(
                                headerWithName(LOCATION).description("Oauth 서버 리다이렉트 URL")
                        )
                ))
                .andDo(print());
    }

    // FIXME: 2023/09/15 RFC2616 버전오류 해결해주세요.
    @Disabled
    @DisplayName("Github 소셜 로그인을 위해 AuthCode 를 받아 SocialToken 으로 교환하여 Github 프로필 정보를 찾아오고 미가입 사용자일 경우 자동으로 회원가입을 진행하고 JWT 로 변환하여 클라이언트에게 넘겨준다.")
    @Test
    void github_login() throws Exception {
        // given & when
        final RefreshToken refreshToken = RefreshToken.builder()
                .member(mock(Member.class))
                .token(new Token("mock refresh token"))
                .expireDate(new ExpireDate(LocalDateTime.now().plusDays(30)))
                .build();
        final Tokens tokens = new Tokens(new AccessToken("Bearer Jwt"), refreshToken);

        when(oauthService.login(GITHUB, "authcode"))
                .thenReturn(tokens);

        // then
        mockMvc.perform(get("/api/v1/oauth/login/{oauthType}", "github")
                        .queryParam("code", "authcode")
                        .contentType(APPLICATION_JSON)
                        .characterEncoding(UTF_8)
                )
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                                pathParameters(
                                        parameterWithName("oauthType").description("소셜 로그인 타입")
                                ),
                                queryParameters(
                                        parameterWithName("code").description("소셜로부터 redirect 하여 받은 AuthCode")
                                ),
                                responseHeaders(
                                        headerWithName("Authorization").description("발급된 JWT 토큰")
                                ),
                                responseCookies(
                                        cookieWithName("refreshToken").description("발급된 리프레시 토큰")
                                )
                        )
                )
                .andDo(print());
    }
}
