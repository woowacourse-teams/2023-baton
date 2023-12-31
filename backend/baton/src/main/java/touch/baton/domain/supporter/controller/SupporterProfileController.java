package touch.baton.domain.supporter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import touch.baton.domain.oauth.controller.resolver.AuthSupporterPrincipal;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.controller.response.SupporterResponse;
import touch.baton.domain.supporter.service.SupporterService;
import touch.baton.domain.supporter.service.dto.SupporterUpdateRequest;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/v1/profile/supporter")
@RestController
public class SupporterProfileController {

    private final SupporterService supporterService;

    @GetMapping("/{supporterId}")
    public ResponseEntity<SupporterResponse.Profile> readProfileBySupporterId(@PathVariable final Long supporterId) {
        final Supporter foundSupporter = supporterService.readBySupporterId(supporterId);
        final SupporterResponse.Profile response = SupporterResponse.Profile.from(foundSupporter);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<SupporterResponse.MyProfile> readSupporterMyProfileByLoginToken(@AuthSupporterPrincipal final Supporter loginedSupporter) {
        final SupporterResponse.MyProfile response = SupporterResponse.MyProfile.from(loginedSupporter);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me")
    public ResponseEntity<Void> updateProfile(@AuthSupporterPrincipal final Supporter supporter,
                                              @RequestBody @Valid final SupporterUpdateRequest request) {
        supporterService.updateSupporter(supporter, request);
        final URI redirectUri = UriComponentsBuilder.fromPath("/api/v1/profile/supporter/me").build().toUri();
        return ResponseEntity.noContent().location(redirectUri).build();
    }
}
