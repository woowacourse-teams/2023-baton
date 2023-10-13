package touch.baton.domain.member.query.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.query.controller.response.RunnerResponse;
import touch.baton.domain.member.query.service.RunnerQueryService;
import touch.baton.domain.oauth.query.controller.resolver.AuthRunnerPrincipal;

@RequiredArgsConstructor
@RequestMapping("/api/v1/profile/runner")
@RestController
public class RunnerQueryController {

    private final RunnerQueryService runnerQueryService;

    @GetMapping("/me")
    public ResponseEntity<RunnerResponse.Mine> readMyProfileByToken(@AuthRunnerPrincipal Runner runner) {
        final RunnerResponse.Mine response = RunnerResponse.Mine.from(runner);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{runnerId}")
    public ResponseEntity<RunnerResponse.Detail> readRunnerProfile(@PathVariable Long runnerId) {
        final Runner runner = runnerQueryService.readByRunnerId(runnerId);
        final RunnerResponse.Detail response = RunnerResponse.Detail.from(runner);
        return ResponseEntity.ok(response);
    }
}
