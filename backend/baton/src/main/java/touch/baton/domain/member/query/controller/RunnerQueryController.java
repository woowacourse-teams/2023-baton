package touch.baton.domain.member.query.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.query.controller.response.RunnerProfileResponse;
import touch.baton.domain.member.query.controller.response.RunnerResponse;
import touch.baton.domain.member.query.service.RunnerQueryService;
import touch.baton.domain.oauth.query.controller.resolver.AuthRunnerPrincipal;

@RequiredArgsConstructor
@RequestMapping("/api/v1/profile/runner")
@RestController
public class RunnerQueryController {

    private final RunnerQueryService runnerQueryService;

    @GetMapping("/me")
    public ResponseEntity<RunnerResponse.MyProfile> readMyProfileByToken(@AuthRunnerPrincipal Runner runner) {
        final RunnerResponse.MyProfile response = RunnerResponse.MyProfile.from(runner);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{runnerId}")
    public ResponseEntity<RunnerProfileResponse.Detail> readRunnerProfile(@PathVariable Long runnerId) {
        final Runner runner = runnerQueryService.readByRunnerId(runnerId);
        return ResponseEntity.ok(RunnerProfileResponse.Detail.from(runner));
    }
}
