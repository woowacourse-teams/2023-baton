package touch.baton.domain.oauth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
@RestController
public class OauthController {

    private final OauthService oauthService;

    @GetMapping("/{oauthType}")
    public ResponseEntity<Void> redirectAuthCode(@PathVariable("oauthType") final OauthType oauthType,
                                                 final HttpServletResponse response
    ) throws IOException {
        final String redirectUrl = oauthService.readAuthCodeRedirect(oauthType);
        response.sendRedirect(redirectUrl);

        return ResponseEntity.status(FOUND).build();
    }

    /*
     * - [x] access Token 을 발급할 때 refresh Token 도 쿠키로 반환 하게 함
     *  - [x] test
     * - [] access Token 이 만료되면 refresh + access token으로 재발급 받는 로직 추가
     *  - [] test
     * - [x] access Token 만료 기간 변경 -> 30분
     *  - [x] test
     */
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

    private void setCookie(final HttpServletResponse response, final RefreshToken refreshToken) {
        final Cookie cookie = new Cookie("refreshToken", refreshToken.getToken().getValue());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        // FIXME: 2023/09/10 적절한 값으로 수명 설정
        cookie.setMaxAge(Duration.ofDays(14).toSecondsPart());

        response.addCookie(cookie);
    }
}
