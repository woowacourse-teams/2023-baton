package touch.baton.domain.runnerpost.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.domain.common.exception.ClientRequestException;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.service.RunnerPostReadService;
import touch.baton.domain.runnerpost.vo.ReviewStatus;

import java.util.List;

import static touch.baton.domain.common.exception.ClientErrorCode.INVALID_QUERY_STRING_FORMAT;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/runner")
@RestController
public class RunnerPostReadController {

    private final RunnerPostReadService runnerPostReadService;

    @GetMapping("/tags/search")
    public ResponseEntity<List<RunnerPostResponse.Simple>> readRunnerPostsByTagNamesAndReviewStatus(
            @RequestParam(required = false) final String tagName,
            @RequestParam(required = false) final Long cursor,
            @RequestParam final int limit,
            @RequestParam final ReviewStatus reviewStatus
    ) {
        if (isTagNameBlank(tagName) && isFirstPage(cursor)) {
            return ResponseEntity.ok(runnerPostReadService.readLatestByLimitAndReviewStatus(limit, reviewStatus));
        }
        if (isTagNameBlank(tagName) && !isFirstPage(cursor)) {
            return ResponseEntity.ok(runnerPostReadService.readRunnerPostByPageInfoAndReviewStatus(cursor, limit, reviewStatus));
        }
        if (!isTagNameBlank(tagName) && isFirstPage(cursor)) {
            return null;
        }
        if (!isTagNameBlank(tagName) && !isFirstPage(cursor)) {
            return null;
        }
        throw new ClientRequestException(INVALID_QUERY_STRING_FORMAT);
    }

    private boolean isTagNameBlank(final String tagName) {
        return tagName == null || tagName.isBlank();
    }

    private boolean isFirstPage(final Long cursor) {
        return cursor == null;
    }
}
