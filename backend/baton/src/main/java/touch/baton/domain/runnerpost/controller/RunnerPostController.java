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
}
