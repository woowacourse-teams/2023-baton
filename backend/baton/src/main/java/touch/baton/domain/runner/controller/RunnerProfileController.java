package touch.baton.domain.runner.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.domain.oauth.controller.resolver.AuthRunnerPrincipal;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.controller.response.RunnerMyProfileResponse;
import touch.baton.domain.runner.controller.response.RunnerResponse;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.service.RunnerPostService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/profile/runner")
@RestController
public class RunnerProfileController {

    private final RunnerPostService runnerPostService;

    @GetMapping
    public ResponseEntity<RunnerMyProfileResponse> readMyProfile(@AuthRunnerPrincipal final Runner runner) {
        final RunnerResponse.Mine me = RunnerResponse.Mine.from(runner);
        final List<RunnerPostResponse.Mine> runnerPosts = runnerPostService.readRunnerPostsByRunnerId(runner.getId()).stream()
                .map(RunnerPostResponse.Mine::from)
                .toList();
        return ResponseEntity.ok(new RunnerMyProfileResponse(me, runnerPosts));
    }
}
