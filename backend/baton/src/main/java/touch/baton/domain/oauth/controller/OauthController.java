package touch.baton.domain.oauth.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.domain.member.vo.OauthId;
import touch.baton.domain.oauth.OauthType;
import touch.baton.domain.oauth.service.OauthService;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("/oauth")
@RestController
public class OauthController {

    private final OauthService oauthService;

    @GetMapping("/{oauthType}")
    public ResponseEntity<Void> redirectAuthCode(
            @PathVariable("oauthType") OauthType oauthType,
            HttpServletResponse response
    ) throws IOException {
        final String redirectUrl = oauthService.readAuthCodeRedirect(oauthType);
        response.sendRedirect(redirectUrl);

        return ResponseEntity.ok().build();
    }

    /**
     * 이 부분에서 oauthId가 노출되지 않게 JWT으로 만들어서 넘겨주면 되는가!
     */
    @GetMapping("/login/{oauthType}")
    public ResponseEntity<String> login(
            @PathVariable OauthType oauthType,
            @RequestParam("code") String code
    ) {
        final OauthId oauthId = oauthService.login(oauthType, code);

        return ResponseEntity.ok()
                .header("accessToken", oauthId.getValue())
                .build();
    }
}
