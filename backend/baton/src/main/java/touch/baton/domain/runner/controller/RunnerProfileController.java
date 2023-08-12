package touch.baton.domain.runner.controller;

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
import touch.baton.domain.oauth.controller.resolver.AuthRunnerPrincipal;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.controller.response.RunnerMyProfileResponse;
import touch.baton.domain.runner.controller.response.RunnerProfileResponse;
import touch.baton.domain.runner.controller.response.RunnerResponse;
import touch.baton.domain.runner.service.RunnerService;
import touch.baton.domain.runner.service.dto.RunnerUpdateRequest;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.service.RunnerPostService;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/profile/runner")
@RestController
public class RunnerProfileController {

    private final RunnerPostService runnerPostService;
    private final RunnerService runnerService;

    @GetMapping
    public ResponseEntity<RunnerMyProfileResponse> readMyProfile(@AuthRunnerPrincipal final Runner runner) {
        final RunnerResponse.Mine me = RunnerResponse.Mine.from(runner);
        final List<RunnerPostResponse.Mine> runnerPosts = runnerPostService.readRunnerPostsByRunnerId(runner.getId()).stream()
                .map(RunnerPostResponse.Mine::from)
                .toList();
        return ResponseEntity.ok(new RunnerMyProfileResponse(me, runnerPosts));
    }

    @GetMapping("/me")
    public ResponseEntity<RunnerResponse.MyProfile> readMyProfileByToken(@AuthRunnerPrincipal Runner runner) {
        final RunnerResponse.MyProfile response = RunnerResponse.MyProfile.from(runner);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me")
    public ResponseEntity<Void> updateMyProfile(@AuthRunnerPrincipal final Runner runner,
                                                @RequestBody @Valid final RunnerUpdateRequest runnerUpdateRequest) {
        runnerService.updateRunner(runner, runnerUpdateRequest);
        final URI redirectUri = UriComponentsBuilder.fromPath("/api/v1/profile/runner/me").build().toUri();
        return ResponseEntity.noContent().location(redirectUri).build();
    }

    @GetMapping("/{runnerId}")
    public ResponseEntity<RunnerProfileResponse.Detail> readRunnerProfile(@PathVariable Long runnerId) {
        final Runner runner = runnerService.readByRunnerId(runnerId);
        return ResponseEntity.ok(RunnerProfileResponse.Detail.from(runner));
    }
}
