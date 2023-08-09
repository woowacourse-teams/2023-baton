package touch.baton.domain.supporter.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.controller.response.SupporterResponse;
import touch.baton.domain.supporter.service.SupporterService;

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
}
