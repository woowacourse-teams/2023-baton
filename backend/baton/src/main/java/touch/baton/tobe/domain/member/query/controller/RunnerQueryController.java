package touch.baton.tobe.domain.member.query.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.tobe.domain.member.command.Runner;
import touch.baton.tobe.domain.member.query.controller.response.RunnerMyProfileResponse;
import touch.baton.tobe.domain.member.query.controller.response.RunnerProfileResponse;
import touch.baton.tobe.domain.member.query.controller.response.RunnerResponse;
import touch.baton.tobe.domain.member.query.service.RunnerQueryService;
import touch.baton.tobe.domain.oauth.query.controller.resolver.AuthRunnerPrincipal;
import touch.baton.tobe.domain.runnerpost.command.controller.response.RunnerPostResponse;
import touch.baton.tobe.domain.runnerpost.query.service.RunnerPostQueryService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/profile/runner")
@RestController
public class RunnerQueryController {

    private final RunnerPostQueryService runnerPostQueryService;
    private final RunnerQueryService runnerQueryService;

    // FIXME: 2023/09/26 runnerPostQueryService로 옮기기
    @GetMapping
    public ResponseEntity<RunnerMyProfileResponse> readMyProfile(@AuthRunnerPrincipal final Runner runner) {
        final RunnerResponse.Mine me = RunnerResponse.Mine.from(runner);
        final List<RunnerPostResponse.Mine> runnerPosts = runnerPostQueryService.readRunnerPostsByRunnerId(runner.getId()).stream()
                .map(RunnerPostResponse.Mine::from)
                .toList();
        return ResponseEntity.ok(new RunnerMyProfileResponse(me, runnerPosts));
    }

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
