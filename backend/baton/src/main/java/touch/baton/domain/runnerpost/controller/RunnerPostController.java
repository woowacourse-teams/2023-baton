package touch.baton.domain.runnerpost.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.domain.common.response.RunnerPostReadResponse;
import touch.baton.domain.common.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.service.RunnerPostService;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/runner")
@RestController
public class RunnerPostController {

    private final RunnerPostService runnerPostService;

    @GetMapping("/posts")
    public ResponseEntity<RunnerPostReadResponse> readAllRunnerPosts() {
        final RunnerPostReadResponse foundRunnerPosts = RunnerPostReadResponse
                .fromRunnerPostResponses(RunnerPostResponse
                        .fromRunnerPosts(runnerPostService.readAllRunnerPosts()));

        return ResponseEntity.ok(foundRunnerPosts);
    }

    @GetMapping("/{runnerId}/posts")
    public ResponseEntity<RunnerPostReadResponse> findRunnerPostsByRunnerId(@PathVariable Long runnerId) {
        final RunnerPostReadResponse foundRunnerPosts = RunnerPostReadResponse
                .fromRunnerPostResponses(RunnerPostResponse
                        .fromRunnerPosts(runnerPostService.readRunnerPostsByRunnerId(runnerId)));

        return ResponseEntity.ok(foundRunnerPosts);
    }

    @GetMapping("/posts/{supporterId}")
    public ResponseEntity<RunnerPostReadResponse> findRunnerPostsBySupporterId(@PathVariable Long supporterId) {
        final RunnerPostReadResponse foundRunnerPosts = RunnerPostReadResponse
                .fromRunnerPostResponses(RunnerPostResponse
                        .fromRunnerPosts(runnerPostService.readRunnerPostsBySupporterId(supporterId)));

        return ResponseEntity.ok(foundRunnerPosts);
    }

    @GetMapping("/posts/{title}")
    public ResponseEntity<RunnerPostReadResponse> findRunnerPostByTitle(@PathVariable String title) {
        final RunnerPostReadResponse foundRunnerPosts = RunnerPostReadResponse
                .fromRunnerPostResponse(RunnerPostResponse
                        .fromRunnerPost(runnerPostService.readRunnerPostByTitle(title)));

        return ResponseEntity.ok(foundRunnerPosts);
    }
}
