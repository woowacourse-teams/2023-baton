package touch.baton.domain.runnerpost.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import touch.baton.domain.common.response.PageResponse;
import touch.baton.domain.oauth.controller.resolver.AuthRunnerPrincipal;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.controller.response.RunnerPostReadResponses;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.service.RunnerPostService;
import touch.baton.domain.runnerpost.service.dto.RunnerPostCreateRequest;
import touch.baton.domain.runnerpost.service.dto.RunnerPostCreateTestRequest;
import touch.baton.domain.runnerpost.service.dto.RunnerPostUpdateRequest;
import touch.baton.domain.runnerpost.vo.ReviewStatus;

import java.net.URI;
import java.util.List;
import java.util.stream.IntStream;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/runner")
@RestController
public class RunnerPostController {

    private final RunnerPostService runnerPostService;

    @PostMapping
    public ResponseEntity<Void> createRunnerPost(@AuthRunnerPrincipal final Runner runner,
                                                 @Valid @RequestBody final RunnerPostCreateRequest request
    ) {
        final Long savedId = runnerPostService.createRunnerPost(runner, request);
        final URI redirectUri = UriComponentsBuilder.fromPath("/api/v1/posts/runner")
                .path("/{id}")
                .buildAndExpand(savedId)
                .toUri();
        return ResponseEntity.created(redirectUri).build();
    }

    @PostMapping("/test")
    public ResponseEntity<Void> createRunnerPostVersionTest(@AuthRunnerPrincipal final Runner runner,
                                                            @Valid @RequestBody final RunnerPostCreateTestRequest request
    ) {
        final Long savedId = runnerPostService.createRunnerPostTest(runner, request);
        final URI redirectUri = UriComponentsBuilder.fromPath("/api/v1/posts/runner")
                .path("/{id}")
                .buildAndExpand(savedId)
                .toUri();
        return ResponseEntity.created(redirectUri).build();
    }

    @GetMapping("/{runnerPostId}")
    public ResponseEntity<RunnerPostResponse.Detail> readByRunnerPostId(@AuthRunnerPrincipal(required = false) final Runner runner,
                                                                        @PathVariable final Long runnerPostId
    ) {
        final RunnerPost runnerPost = runnerPostService.readByRunnerPostId(runnerPostId);
        runnerPostService.increaseWatchedCount(runnerPost);

        final RunnerPostResponse.Detail response = RunnerPostResponse.Detail.of(
                runnerPost,
                runnerPost.getRunner().equals(runner)
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{runnerPostId}/test")
    public ResponseEntity<RunnerPostResponse.DetailVersionTest> readByRunnerPostIdVersionTest(@AuthRunnerPrincipal(required = false) final Runner runner,
                                                                                              @PathVariable final Long runnerPostId
    ) {
        final RunnerPost runnerPost = runnerPostService.readByRunnerPostId(runnerPostId);
        runnerPostService.increaseWatchedCount(runnerPost);

        final RunnerPostResponse.DetailVersionTest response = RunnerPostResponse.DetailVersionTest.ofVersionTest(
                runnerPost,
                runnerPost.getRunner().equals(runner)
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{runnerPostId}")
    public ResponseEntity<Void> deleteByRunnerPostId(@AuthRunnerPrincipal final Runner runner,
                                                     @PathVariable final Long runnerPostId
    ) {
        runnerPostService.deleteByRunnerPostId(runnerPostId, runner);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{runnerPostId}")
    public ResponseEntity<Void> update(@AuthRunnerPrincipal Runner runner,
                                       @PathVariable final Long runnerPostId,
                                       @Valid @RequestBody final RunnerPostUpdateRequest request
    ) {
        final Long updatedId = runnerPostService.updateRunnerPost(runnerPostId, runner, request);
        final URI redirectUri = UriComponentsBuilder.fromPath("/api/v1/posts/runner")
                .path("/{runnerPostId}")
                .buildAndExpand(updatedId)
                .toUri();
        return ResponseEntity.created(redirectUri).build();
    }

    @GetMapping
    public ResponseEntity<RunnerPostReadResponses.NoFiltering> readAllRunnerPosts() {
        final List<RunnerPostResponse.Simple> responses = runnerPostService.readAllRunnerPosts().stream()
                .map(RunnerPostResponse.Simple::from)
                .toList();

        return ResponseEntity.ok(RunnerPostReadResponses.NoFiltering.from(responses));
    }

    @GetMapping("/test")
    public ResponseEntity<RunnerPostReadResponses.NoFiltering> readAllRunnerPostsVersionTest() {
        final List<RunnerPostResponse.Simple> responses = runnerPostService.readAllRunnerPosts().stream()
                .map(RunnerPostResponse.Simple::from)
                .toList();

        return ResponseEntity.ok(RunnerPostReadResponses.NoFiltering.from(responses));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<RunnerPostResponse.ReferencedBySupporter>> readReferencedBySupporter(
            @PageableDefault(size = 10, page = 1, sort = "createdAt", direction = DESC) final Pageable pageable,
            @RequestParam("supporterId") final Long supporterId,
            @RequestParam("reviewStatus") final ReviewStatus reviewStatus
    ) {
        final Page<RunnerPost> pageRunnerPosts = runnerPostService.readRunnerPostsBySupporterIdAndReviewStatus(pageable, supporterId, reviewStatus);
        final List<RunnerPost> foundRunnerPosts = pageRunnerPosts.getContent();
        final List<Integer> applicantCounts = collectApplicantCounts(pageRunnerPosts);
        final List<RunnerPostResponse.ReferencedBySupporter> responses = IntStream.range(0, foundRunnerPosts.size())
                .mapToObj(index -> {
                    final RunnerPost foundRunnerPost = foundRunnerPosts.get(index);
                    final Integer applicantCount = applicantCounts.get(index);

                    return RunnerPostResponse.ReferencedBySupporter.of(foundRunnerPost, applicantCount);
                }).toList();

        final Page<RunnerPostResponse.ReferencedBySupporter> pageResponse
                = new PageImpl<>(responses, pageable, pageRunnerPosts.getTotalPages());

        return ResponseEntity.ok(PageResponse.from(pageResponse));
    }

    private List<Integer> collectApplicantCounts(final Page<RunnerPost> pageRunnerPosts) {
        final List<Long> runnerPostIds = pageRunnerPosts.stream()
                .map(RunnerPost::getId)
                .toList();

        return runnerPostService.readCountsByRunnerPostIds(runnerPostIds);
    }
}
