package touch.baton.domain.runnerpost.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.domain.common.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.service.RunnerPostService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/runner")
@RestController
public class RunnerPostController {

    private final RunnerPostService runnerPostService;

    //프로필 아직 안 만들어서 프로필, 프로필 이미지 제외
    @GetMapping("/posts")
    public ResponseEntity<List<RunnerPostResponse>> read() {
        List<RunnerPost> runnerPosts = runnerPostService.read();
        List<RunnerPostResponse> response = RunnerPostResponse.convert(runnerPosts);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{runnerId}/posts")
    public ResponseEntity<List<RunnerPostResponse>> findByRunnerId(@RequestParam Long id) {
        List<RunnerPost> runnerPosts = runnerPostService.findByRunnerId(id);
        List<RunnerPostResponse> response = RunnerPostResponse.convert(runnerPosts);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts/{supporterId}")
    public ResponseEntity<List<RunnerPostResponse>> findBySupporterId(@RequestParam Long supporterId) {
        List<RunnerPost> runnerPosts = runnerPostService.findBySupporterId(supporterId);
        List<RunnerPostResponse> response = RunnerPostResponse.convert(runnerPosts);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts/{title}")
    public ResponseEntity<RunnerPostResponse> findByTitle(@RequestParam String title) {
        RunnerPost runnerPost = runnerPostService.findByTitle(title);
        RunnerPostResponse response = RunnerPostResponse.fromRunnerPost(runnerPost);

        return ResponseEntity.ok(response);
    }
}
