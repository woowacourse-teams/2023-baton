package touch.baton.domain.supporter.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.domain.supporter.controller.response.SupporterReadResponses;
import touch.baton.domain.supporter.controller.response.SupporterResponse;
import touch.baton.domain.supporter.service.SupporterService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/supporters")
@RestController
public class SupporterController {

    private final SupporterService supporterService;

    @GetMapping("/test")
    public ResponseEntity<SupporterReadResponses.NoFiltering> readAll() {
        final List<SupporterResponse.Detail> responses = supporterService.readAllSupporters().stream()
                .map(SupporterResponse.Detail::from)
                .toList();

        return ResponseEntity.ok(SupporterReadResponses.NoFiltering.from(responses));
    }
}
