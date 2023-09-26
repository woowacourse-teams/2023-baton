package touch.baton.tobe.domain.member.command.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import touch.baton.tobe.domain.member.command.Runner;
import touch.baton.tobe.domain.member.command.service.RunnerCommandService;
import touch.baton.tobe.domain.member.command.service.dto.RunnerUpdateRequest;
import touch.baton.tobe.domain.oauth.query.controller.resolver.AuthRunnerPrincipal;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/v1/profile/runner")
@RestController
public class RunnerCommandController {

    private final RunnerCommandService runnerCommandService;

    @PatchMapping("/me")
    public ResponseEntity<Void> updateMyProfile(@AuthRunnerPrincipal final Runner runner,
                                                @RequestBody @Valid final RunnerUpdateRequest runnerUpdateRequest) {
        runnerCommandService.updateRunner(runner, runnerUpdateRequest);
        final URI redirectUri = UriComponentsBuilder.fromPath("/api/v1/profile/runner/me").build().toUri();
        return ResponseEntity.noContent().location(redirectUri).build();
    }
}
