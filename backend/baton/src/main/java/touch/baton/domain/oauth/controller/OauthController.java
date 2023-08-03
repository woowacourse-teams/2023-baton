package touch.baton.domain.oauth.controller;

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

import java.io.IOException;

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

    @GetMapping("/login/{oauthType}")
    public ResponseEntity<Void> login(@PathVariable final OauthType oauthType,
                                      @RequestParam final String code
    ) {
        final String jwtToken = oauthService.login(oauthType, code);

        return ResponseEntity.ok()
                .header(AUTHORIZATION, jwtToken)
                .build();
    }
}
