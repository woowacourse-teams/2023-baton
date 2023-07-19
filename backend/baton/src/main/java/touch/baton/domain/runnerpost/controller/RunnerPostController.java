package touch.baton.domain.runnerpost.controller;

import lombok.RequiredArgsConstructor;
 import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.service.RunnerPostService;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/runner")
@RestController
public class RunnerPostController {

    private final RunnerPostService runnerPostService;

    @GetMapping("/{runner_post_id}")
    public ResponseEntity<RunnerPostResponse.SingleRunnerPost> readByRunnerPostId(
            @PathVariable(name = "runner_post_id") final Long runnerPostId
    ) {
        final RunnerPostResponse.SingleRunnerPost response
                = RunnerPostResponse.SingleRunnerPost.from(runnerPostService.readByRunnerPostId(runnerPostId));

        return ResponseEntity
                .ok(response);
    }

    @DeleteMapping("/{runner_post_id}")
    public ResponseEntity<Void> deleteByRunnerPostId(
            @PathVariable(name = "runner_post_id") final Long runnerPostId
    ) {
        runnerPostService.deleteByRunnerPostId(runnerPostId);

        return ResponseEntity
                .noContent()
                .build();
    }
}
