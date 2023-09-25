package touch.baton.domain.runnerpost.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponses;
import touch.baton.domain.runnerpost.service.RunnerPostReadService;
import touch.baton.domain.runnerpost.service.RunnerPostService;
import touch.baton.domain.runnerpost.vo.ReviewStatus;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/runner")
@RestController
public class RunnerPostReadController {

    private final RunnerPostReadService runnerPostReadService;
    private final RunnerPostService runnerPostService;

    @GetMapping
    public ResponseEntity<RunnerPostResponses.Simple> readRunnerPostsByTagNamesAndReviewStatus(
            @RequestParam(required = false) final String tagName,
            @RequestParam(required = false) final Long cursor,
            @RequestParam final int limit,
            @RequestParam final ReviewStatus reviewStatus
    ) {
        return ResponseEntity.ok(runnerPostReadService.readRunnerPostByPageInfoAndTagNameAndReviewStatus(tagName, cursor, limit, reviewStatus));
    }

    private boolean isTagNameBlank(final String tagName) {
        return tagName == null || tagName.isBlank();
    }

    private boolean isFirstPage(final Long cursor) {
        return cursor == null;
    }
}
