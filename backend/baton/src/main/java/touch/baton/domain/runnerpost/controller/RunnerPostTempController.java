package touch.baton.domain.runnerpost.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.service.RunnerPostNativeService;
import touch.baton.domain.runnerpost.vo.ReviewStatus;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class RunnerPostTempController {

    private final RunnerPostNativeService runnerPostNativeService;

    @GetMapping("/temp")
    public ResponseEntity<List<RunnerPostResponse.Simple>> read(
            @RequestParam("cursor") final Long cursor,
            @RequestParam("limit") final Long limit,
            @RequestParam("reviewStatus") final ReviewStatus reviewStatus
    ) {
        final List<RunnerPost> runnerPosts = runnerPostNativeService.findNative(cursor, limit, reviewStatus);
        final List<RunnerPostResponse.Simple> response = runnerPosts.stream()
                .map(runnerPost -> RunnerPostResponse.Simple.from(runnerPost, 0L))
                .toList();
        return ResponseEntity.ok(response);
    }
}
