package touch.baton.domain.oauth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.domain.oauth.OauthType;
import touch.baton.domain.oauth.service.OauthService;
import touch.baton.domain.oauth.token.RefreshToken;
import touch.baton.domain.oauth.token.Tokens;

import java.io.IOException;
import java.time.Duration;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FOUND;
import static touch.baton.domain.oauth.token.RefreshToken.REFRESH_TOKEN_LIFECYCLE;

@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
@RestController
public class OauthController {

    private static final String REFRESH_TOKEN_KEY = "refreshToken";

    private final OauthService oauthService;

    @GetMapping("/{oauthType}")
    public ResponseEntity<Void> redirectAuthCode(@PathVariable("oauthType") final OauthType oauthType,
                                                 final HttpServletResponse response
    ) throws IOException {
        final String redirectUrl = oauthService.readAuthCodeRedirect(oauthType);
        response.sendRedirect(redirectUrl);

        return ResponseEntity.status(FOUND).build();
    }

    @GetMapping("/login/{oauthType}")
    public ResponseEntity<Void> login(@PathVariable final OauthType oauthType,
                                      @RequestParam final String code,
                                      final HttpServletResponse response
    ) {
        final Tokens tokens = oauthService.login(oauthType, code);

        setCookie(response, tokens.refreshToken());

        return ResponseEntity.ok()
                .header(AUTHORIZATION, tokens.accessToken().getValue())
                .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshJwt(@CookieValue final String refreshToken,
                                           final HttpServletRequest request,
                                           final HttpServletResponse response
    ) {
        final String authHeader = request.getHeader(AUTHORIZATION);
        final Tokens tokens = oauthService.reissueAccessToken(authHeader, refreshToken);

        setCookie(response, tokens.refreshToken());

        return ResponseEntity.noContent()
                .header(AUTHORIZATION, tokens.accessToken().getValue())
                .build();
    }

    private void setCookie(final HttpServletResponse response, final RefreshToken refreshToken) {
        final ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken.getToken().getValue())
                .httpOnly(true)
                .secure(true)
                .maxAge(Duration.ofDays(REFRESH_TOKEN_LIFECYCLE).toSeconds())
                .sameSite(SameSite.NONE.attributeValue())
                .path("/")
                .build();
        response.addHeader("Set-Cookie", responseCookie.toString());
    }
}
