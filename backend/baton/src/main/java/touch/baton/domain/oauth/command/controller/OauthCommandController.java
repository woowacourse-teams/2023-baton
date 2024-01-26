package touch.baton.domain.oauth.command.controller;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.exception.ClientRequestException;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.oauth.command.AuthorizationHeader;
import touch.baton.domain.oauth.command.OauthType;
import touch.baton.domain.oauth.command.service.OauthCommandService;
import touch.baton.domain.oauth.command.token.RefreshToken2;
import touch.baton.domain.oauth.command.token.Tokens;
import touch.baton.domain.oauth.query.controller.resolver.AuthMemberPrincipal;

import java.io.IOException;

import static org.springframework.boot.web.server.Cookie.SameSite.NONE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpStatus.FOUND;

@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
@RestController
public class OauthCommandController {

    private final OauthCommandService oauthCommandService;

    @GetMapping("/{oauthType}")
    public ResponseEntity<Void> redirectAuthCode(@PathVariable("oauthType") final OauthType oauthType,
                                                 final HttpServletResponse response
    ) throws IOException {
        final String redirectUrl = oauthCommandService.readAuthCodeRedirect(oauthType);
        response.sendRedirect(redirectUrl);

        return ResponseEntity.status(FOUND).build();
    }

    @GetMapping("/login/{oauthType}")
    public ResponseEntity<Void> login(@PathVariable final OauthType oauthType,
                                      @RequestParam final String code,
                                      final HttpServletResponse response
    ) {
        final Tokens tokens = oauthCommandService.login(oauthType, code);

        setCookie(response, tokens.refreshToken());

        return ResponseEntity.ok()
                .header(AUTHORIZATION, tokens.accessToken().getValue())
                .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshJwt(@Nullable @CookieValue(required = false) final String refreshToken,
                                           final HttpServletRequest request,
                                           final HttpServletResponse response
    ) {
        if (request.getHeader(AUTHORIZATION) == null) {
            throw new ClientRequestException(ClientErrorCode.OAUTH_AUTHORIZATION_VALUE_IS_NULL);
        }
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new ClientRequestException(ClientErrorCode.REFRESH_TOKEN_IS_NOT_NULL);
        }

        final AuthorizationHeader authorizationHeader = new AuthorizationHeader(request.getHeader(AUTHORIZATION));

        final Tokens tokens = oauthCommandService.reissueAccessToken(authorizationHeader);

        setCookie(response, tokens.refreshToken());

        return ResponseEntity.noContent()
                .header(AUTHORIZATION, tokens.accessToken().getValue())
                .build();
    }

    private void setCookie(final HttpServletResponse response, final RefreshToken2 refreshToken) {
        final ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken.getToken().getValue())
                .httpOnly(true)
                .secure(true)
                .maxAge(refreshToken.getTimeout())
                .sameSite(NONE.attributeValue())
                .path("/")
                .build();
        response.addHeader(SET_COOKIE, responseCookie.toString());
    }

    @PatchMapping("/logout")
    public ResponseEntity<Void> logout(@AuthMemberPrincipal final Member member) {
        oauthCommandService.logout(member);

        return ResponseEntity.noContent().build();
    }
}
