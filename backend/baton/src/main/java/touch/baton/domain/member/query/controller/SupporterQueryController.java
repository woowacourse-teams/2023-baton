package touch.baton.domain.member.query.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.query.controller.response.SupporterResponse;
import touch.baton.domain.member.query.service.SupporterQueryService;
import touch.baton.domain.oauth.query.controller.resolver.AuthSupporterPrincipal;

@RequiredArgsConstructor
@RequestMapping("/api/v1/profile/supporter")
@RestController
public class SupporterQueryController {

    private final SupporterQueryService supporterQueryService;

    @GetMapping("/{supporterId}")
    public ResponseEntity<SupporterResponse.Profile> readProfileBySupporterId(@PathVariable final Long supporterId) {
        final Supporter foundSupporter = supporterQueryService.readBySupporterId(supporterId);
        final SupporterResponse.Profile response = SupporterResponse.Profile.from(foundSupporter);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<SupporterResponse.MyProfile> readSupporterMyProfileByLoginToken(@AuthSupporterPrincipal final Supporter loginedSupporter) {
        final SupporterResponse.MyProfile response = SupporterResponse.MyProfile.from(loginedSupporter);

        return ResponseEntity.ok(response);
    }
}
