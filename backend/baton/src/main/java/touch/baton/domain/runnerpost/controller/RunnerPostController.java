package touch.baton.domain.runnerpost.controller;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import touch.baton.domain.common.response.PageResponse;
import touch.baton.domain.member.Member;
import touch.baton.domain.oauth.controller.resolver.AuthMemberPrincipal;
import touch.baton.domain.oauth.controller.resolver.AuthRunnerPrincipal;
import touch.baton.domain.oauth.controller.resolver.AuthSupporterPrincipal;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.controller.response.SupporterRunnerPostResponse;
import touch.baton.domain.runnerpost.controller.response.SupporterRunnerPostResponses;
import touch.baton.domain.runnerpost.service.RunnerPostService;
import touch.baton.domain.runnerpost.service.dto.RunnerPostApplicantCreateRequest;
import touch.baton.domain.runnerpost.service.dto.RunnerPostCreateRequest;
import touch.baton.domain.runnerpost.service.dto.RunnerPostCreateTestRequest;
import touch.baton.domain.runnerpost.service.dto.RunnerPostUpdateRequest;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.supporter.Supporter;

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

    @PostMapping("{runnerPostId}/application")
    public ResponseEntity<Void> createRunnerPostApplicant(@AuthSupporterPrincipal final Supporter supporter,
                                                          @PathVariable final Long runnerPostId,
                                                          @RequestBody @Valid final RunnerPostApplicantCreateRequest request
    ) {
        runnerPostService.createRunnerPostApplicant(supporter, request, runnerPostId);

        final URI redirectUri = UriComponentsBuilder.fromPath("/api/v1/posts/runner")
                .path("/{runnerPostId}")
                .buildAndExpand(runnerPostId)
                .toUri();

        return ResponseEntity.created(redirectUri).build();
    }

    @GetMapping("/{runnerPostId}")
    public ResponseEntity<RunnerPostResponse.Detail> readByRunnerPostId(@AuthMemberPrincipal(required = false) final Member member,
                                                                        @PathVariable final Long runnerPostId
    ) {
        final RunnerPost foundRunnerPost = runnerPostService.readByRunnerPostId(runnerPostId);
        final long applicantCount = runnerPostService.readCountByRunnerPostId(foundRunnerPost.getId());
        final boolean isApplicantHistoryExist = runnerPostService.existsRunnerPostApplicantByRunnerPostIdAndMemberId(runnerPostId, member.getId());

        runnerPostService.increaseWatchedCount(foundRunnerPost);
        final RunnerPostResponse.Detail response = RunnerPostResponse.Detail.of(
                foundRunnerPost,
                foundRunnerPost.isNotOwner(member),
                isApplicantHistoryExist,
                applicantCount
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{runnerPostId}/supporters")
    public ResponseEntity<SupporterRunnerPostResponses.Detail> readSupporterRunnerPostsByRunnerPostId(@AuthRunnerPrincipal final Runner runner,
                                                                                                      @PathVariable final Long runnerPostId
    ) {
        final List<SupporterRunnerPostResponse.Detail> responses = runnerPostService.readSupporterRunnerPostsByRunnerPostId(runner, runnerPostId).stream()
                .map(supporterRunnerPost -> SupporterRunnerPostResponse.Detail.from(supporterRunnerPost))
                .toList();

        return ResponseEntity.ok(SupporterRunnerPostResponses.Detail.from(responses));
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
    public ResponseEntity<Void> update(@AuthRunnerPrincipal final Runner runner,
                                       @PathVariable final Long runnerPostId,
                                       @Valid @RequestBody final RunnerPostUpdateRequest.Default request
    ) {
        final Long updatedId = runnerPostService.updateRunnerPost(runnerPostId, runner, request);
        final URI redirectUri = UriComponentsBuilder.fromPath("/api/v1/posts/runner")
                .path("/{runnerPostId}")
                .buildAndExpand(updatedId)
                .toUri();
        return ResponseEntity.created(redirectUri).build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<RunnerPostResponse.Simple>> readAllRunnerPosts(@PageableDefault(size = 10, page = 1, sort = "createdAt", direction = DESC) final Pageable pageable) {
        final Page<RunnerPost> pageRunnerPosts = runnerPostService.readAllRunnerPosts(pageable);
        final List<RunnerPost> foundRunnerPosts = pageRunnerPosts.getContent();
        final List<Long> applicantCounts = collectApplicantCounts(pageRunnerPosts);
        final List<RunnerPostResponse.Simple> responses = IntStream.range(0, foundRunnerPosts.size())
                .mapToObj(index -> {
                    final RunnerPost runnerPost = foundRunnerPosts.get(index);
                    Long applicantCount =  applicantCounts.get(index);

                    return RunnerPostResponse.Simple.from(runnerPost, applicantCount);
                }).toList();

        final Page<RunnerPostResponse.Simple> pageResponse
                = new PageImpl<>(responses, pageable, pageRunnerPosts.getTotalPages());

        return ResponseEntity.ok(PageResponse.from(pageResponse));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<RunnerPostResponse.ReferencedBySupporter>> readReferencedBySupporter(
            @PageableDefault(size = 10, page = 1, sort = "createdAt", direction = DESC) final Pageable pageable,
            @RequestParam("supporterId") final Long supporterId,
            @RequestParam("reviewStatus") final ReviewStatus reviewStatus
    ) {
        final Page<RunnerPost> pageRunnerPosts = runnerPostService.readRunnerPostsBySupporterIdAndReviewStatus(pageable, supporterId, reviewStatus);
        final List<RunnerPost> foundRunnerPosts = pageRunnerPosts.getContent();
        final List<Long> applicantCounts = collectApplicantCounts(pageRunnerPosts);
        final List<RunnerPostResponse.ReferencedBySupporter> responses = IntStream.range(0, foundRunnerPosts.size())
                .mapToObj(index -> {
                    final RunnerPost foundRunnerPost = foundRunnerPosts.get(index);
                    final long applicantCount = applicantCounts.get(index);

                    return RunnerPostResponse.ReferencedBySupporter.of(foundRunnerPost, applicantCount);
                }).toList();

        final Page<RunnerPostResponse.ReferencedBySupporter> pageResponse
                = new PageImpl<>(responses, pageable, pageRunnerPosts.getTotalPages());

        return ResponseEntity.ok(PageResponse.from(pageResponse));
    }

    @GetMapping("/me/supporter")
    public ResponseEntity<PageResponse<RunnerPostResponse.ReferencedBySupporter>> readRunnerPostsByLoginedSupporterAndReviewStatus(
            @PageableDefault(size = 10, page = 1, sort = "createdAt", direction = DESC) final Pageable pageable,
            @AuthSupporterPrincipal final Supporter supporter,
            @PathParam("reviewStatus") final ReviewStatus reviewStatus
    ) {
        final Page<RunnerPost> pageRunnerPosts = runnerPostService.readRunnerPostsBySupporterIdAndReviewStatus(pageable, supporter.getId(), reviewStatus);
        final List<RunnerPost> foundRunnerPosts = pageRunnerPosts.getContent();
        final List<Long> applicantCounts = collectApplicantCounts(pageRunnerPosts);
        final List<RunnerPostResponse.ReferencedBySupporter> responses = IntStream.range(0, foundRunnerPosts.size())
                .mapToObj(index -> {
                    final RunnerPost foundRunnerPost = foundRunnerPosts.get(index);
                    final Long applicantCount = applicantCounts.get(index);

                    return RunnerPostResponse.ReferencedBySupporter.of(foundRunnerPost, applicantCount);
                }).toList();

        final Page<RunnerPostResponse.ReferencedBySupporter> pageResponse
                = new PageImpl<>(responses, pageable, pageRunnerPosts.getTotalPages());

        return ResponseEntity.ok(PageResponse.from(pageResponse));
    }

    private List<Long> collectApplicantCounts(final Page<RunnerPost> pageRunnerPosts) {
        final List<Long> runnerPostIds = pageRunnerPosts.stream()
                .map(RunnerPost::getId)
                .toList();

        return runnerPostService.readCountsByRunnerPostIds(runnerPostIds);
    }

    @GetMapping("/me/runner")
    public ResponseEntity<PageResponse<RunnerPostResponse.SimpleInMyPage>> readRunnerMyPage(@PageableDefault(size = 10, page = 1, sort = "createdAt", direction = DESC) final Pageable pageable,
                                                                                            @AuthRunnerPrincipal final Runner runner,
                                                                                            @RequestParam("reviewStatus") final ReviewStatus reviewStatus) {
        final Page<RunnerPost> pageRunnerPosts = runnerPostService.readRunnerPostsByRunnerIdAndReviewStatus(pageable, runner.getId(), reviewStatus);
        final List<Long> applicantCounts = collectApplicantCounts(pageRunnerPosts);

        final List<RunnerPostResponse.SimpleInMyPage> responses = IntStream.range(0, pageRunnerPosts.getContent().size())
                .mapToObj(index -> {
                            final Long applicantCount = applicantCounts.get(index);
                            final RunnerPost runnerPost = pageRunnerPosts.getContent().get(index);
                            return RunnerPostResponse.SimpleInMyPage.from(runnerPost, applicantCount);
                        }
                ).toList();

        final Page<RunnerPostResponse.SimpleInMyPage> pageResponse
                = new PageImpl<>(responses, pageable, pageRunnerPosts.getTotalPages());

        return ResponseEntity.ok(PageResponse.from(pageResponse));
    }

    @PatchMapping("/{runnerPostId}/cancelation")
    public ResponseEntity<Void> updateSupporterCancelRunnerPost(@AuthSupporterPrincipal final Supporter supporter,
                                                                @PathVariable final Long runnerPostId
    ) {
        runnerPostService.deleteSupporterRunnerPost(supporter, runnerPostId);
        final URI redirectUri = UriComponentsBuilder.fromPath("/api/v1/posts/runner")
                .path("/{runnerPostId}")
                .buildAndExpand(runnerPostId)
                .toUri();
        return ResponseEntity.noContent().location(redirectUri).build();
    }

    @PatchMapping("/{runnerPostId}/supporters")
    public ResponseEntity<Void> updateRunnerPostAppliedSupporter(@AuthRunnerPrincipal final Runner runner,
                                                                 @PathVariable final Long runnerPostId,
                                                                 @Valid @RequestBody final RunnerPostUpdateRequest.SelectSupporter request
    ) {
        runnerPostService.updateRunnerPostAppliedSupporter(runner, runnerPostId, request);

        final URI redirectUri = UriComponentsBuilder.fromPath("/api/v1/posts/runner")
                .path("/{runnerPostId}")
                .buildAndExpand(runnerPostId)
                .toUri();
        return ResponseEntity.noContent().location(redirectUri).build();
    }

    @PatchMapping("/{runnerPostId}/done")
    public ResponseEntity<Void> updateRunnerPostReviewStatusDone(@AuthSupporterPrincipal final Supporter supporter,
                                                                 @PathVariable final Long runnerPostId
    ) {
        runnerPostService.updateRunnerPostReviewStatusDone(runnerPostId, supporter);

        final URI redirectUri = UriComponentsBuilder.fromPath("/api/v1/posts/runner")
                .path("/{runnerPostId}")
                .buildAndExpand(runnerPostId)
                .toUri();
        return ResponseEntity.noContent().location(redirectUri).build();
    }
}
