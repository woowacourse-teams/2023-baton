package touch.baton.domain.member.command.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.command.service.SupporterCommandService;
import touch.baton.domain.member.command.service.dto.SupporterUpdateRequest;
import touch.baton.domain.oauth.query.controller.resolver.AuthSupporterPrincipal;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/v1/profile/supporter")
@RestController
public class SupporterCommandController {

    private final SupporterCommandService supporterCommandService;

    @PatchMapping("/me")
    public ResponseEntity<Void> updateProfile(@AuthSupporterPrincipal final Supporter supporter,
                                              @RequestBody @Valid final SupporterUpdateRequest request) {
        supporterCommandService.updateSupporter(supporter, request);
        final URI redirectUri = UriComponentsBuilder.fromPath("/api/v1/profile/supporter/me").build().toUri();
        return ResponseEntity.noContent().location(redirectUri).build();
    }
}
