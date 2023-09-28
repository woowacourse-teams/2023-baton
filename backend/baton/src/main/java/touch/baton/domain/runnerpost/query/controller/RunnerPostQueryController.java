package touch.baton.domain.runnerpost.query.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.domain.common.response.PageResponse;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.oauth.query.controller.resolver.AuthMemberPrincipal;
import touch.baton.domain.oauth.query.controller.resolver.AuthRunnerPrincipal;
import touch.baton.domain.oauth.query.controller.resolver.AuthSupporterPrincipal;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.command.controller.response.RunnerPostResponses;
import touch.baton.domain.runnerpost.command.controller.response.SupporterRunnerPostResponse;
import touch.baton.domain.runnerpost.command.controller.response.SupporterRunnerPostResponses;
import touch.baton.domain.runnerpost.command.vo.ReviewStatus;
import touch.baton.domain.runnerpost.query.service.RunnerPostQueryService;
import touch.baton.domain.runnerpost.query.service.dto.PageParams;

import java.util.List;
import java.util.stream.IntStream;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/runner")
@RestController
public class RunnerPostQueryController {

    private final RunnerPostQueryService runnerPostQueryService;

    @GetMapping
    public ResponseEntity<RunnerPostResponses.Simple> readRunnerPostsByTagNameAndReviewStatus(
            @Valid @ModelAttribute PageParams pageParams,
            @RequestParam(required = false) final String tagName,
            @RequestParam(required = false) final ReviewStatus reviewStatus
    ) {
        return ResponseEntity.ok(runnerPostQueryService.readRunnerPostByPageInfoAndTagNameAndReviewStatus(tagName, pageParams, reviewStatus));
    }

    @GetMapping("/{runnerPostId}")
    public ResponseEntity<RunnerPostResponse.Detail> readByRunnerPostId(
            @AuthMemberPrincipal(required = false) final Member member,
            @PathVariable final Long runnerPostId
    ) {
        final RunnerPost foundRunnerPost = runnerPostQueryService.readByRunnerPostId(runnerPostId);
        final long applicantCount = runnerPostQueryService.readCountByRunnerPostId(foundRunnerPost.getId());
        final boolean isApplicantHistoryExist = runnerPostQueryService.existsRunnerPostApplicantByRunnerPostIdAndMemberId(runnerPostId, member.getId());

        runnerPostQueryService.increaseWatchedCount(foundRunnerPost);
        final RunnerPostResponse.Detail response = RunnerPostResponse.Detail.of(
                foundRunnerPost,
                foundRunnerPost.isOwner(member),
                isApplicantHistoryExist,
                applicantCount
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<RunnerPostResponse.ReferencedBySupporter>> readReferencedBySupporter(
            @PageableDefault(sort = "id", direction = DESC) final Pageable pageable,
            @RequestParam("supporterId") final Long supporterId,
            @RequestParam("reviewStatus") final ReviewStatus reviewStatus
    ) {
        final Page<RunnerPost> pageRunnerPosts = runnerPostQueryService.readRunnerPostsBySupporterIdAndReviewStatus(pageable, supporterId, reviewStatus);
        final List<RunnerPost> foundRunnerPosts = pageRunnerPosts.getContent();
        final List<Long> applicantCounts = collectApplicantCounts(pageRunnerPosts);
        final List<RunnerPostResponse.ReferencedBySupporter> responses = IntStream.range(0, foundRunnerPosts.size())
                .mapToObj(index -> {
                    final RunnerPost foundRunnerPost = foundRunnerPosts.get(index);
                    final long applicantCount = applicantCounts.get(index);

                    return RunnerPostResponse.ReferencedBySupporter.of(foundRunnerPost, applicantCount);
                }).toList();

        final Page<RunnerPostResponse.ReferencedBySupporter> pageResponse
                = new PageImpl<>(responses, pageable, pageRunnerPosts.getTotalElements());

        return ResponseEntity.ok(PageResponse.from(pageResponse));
    }

    @GetMapping("/{runnerPostId}/supporters")
    public ResponseEntity<SupporterRunnerPostResponses.Detail> readSupporterRunnerPostsByRunnerPostId(
            @AuthRunnerPrincipal final Runner runner,
            @PathVariable final Long runnerPostId
    ) {
        final List<SupporterRunnerPostResponse.Detail> responses = runnerPostQueryService.readSupporterRunnerPostsByRunnerPostId(runner, runnerPostId).stream()
                .map(SupporterRunnerPostResponse.Detail::from)
                .toList();

        return ResponseEntity.ok(SupporterRunnerPostResponses.Detail.from(responses));
    }

    @GetMapping("/me/supporter")
    public ResponseEntity<PageResponse<RunnerPostResponse.ReferencedBySupporter>> readRunnerPostsByLoginedSupporterAndReviewStatus(
            @PageableDefault(sort = "id", direction = DESC) final Pageable pageable,
            @AuthSupporterPrincipal final Supporter supporter,
            @RequestParam("reviewStatus") final ReviewStatus reviewStatus
    ) {
        final Page<RunnerPost> pageRunnerPosts = runnerPostQueryService.readRunnerPostsBySupporterIdAndReviewStatus(pageable, supporter.getId(), reviewStatus);
        final List<RunnerPost> foundRunnerPosts = pageRunnerPosts.getContent();
        final List<Long> applicantCounts = collectApplicantCounts(pageRunnerPosts);
        final List<RunnerPostResponse.ReferencedBySupporter> responses = IntStream.range(0, foundRunnerPosts.size())
                .mapToObj(index -> {
                    final RunnerPost foundRunnerPost = foundRunnerPosts.get(index);
                    final Long applicantCount = applicantCounts.get(index);

                    return RunnerPostResponse.ReferencedBySupporter.of(foundRunnerPost, applicantCount);
                }).toList();

        final Page<RunnerPostResponse.ReferencedBySupporter> pageResponse
                = new PageImpl<>(responses, pageable, pageRunnerPosts.getTotalElements());

        return ResponseEntity.ok(PageResponse.from(pageResponse));
    }

    @GetMapping("/me/runner")
    public ResponseEntity<PageResponse<RunnerPostResponse.SimpleInMyPage>> readRunnerMyPage(
            @PageableDefault(sort = "id", direction = DESC) final Pageable pageable,
            @AuthRunnerPrincipal final Runner runner,
            @RequestParam("reviewStatus") final ReviewStatus reviewStatus
    ) {
        final Page<RunnerPost> pageRunnerPosts = runnerPostQueryService.readRunnerPostsByRunnerIdAndReviewStatus(pageable, runner.getId(), reviewStatus);
        final List<Long> applicantCounts = collectApplicantCounts(pageRunnerPosts);

        final List<RunnerPostResponse.SimpleInMyPage> responses = IntStream.range(0, pageRunnerPosts.getContent().size())
                .mapToObj(index -> {
                            final Long applicantCount = applicantCounts.get(index);
                            final RunnerPost runnerPost = pageRunnerPosts.getContent().get(index);
                            return RunnerPostResponse.SimpleInMyPage.from(runnerPost, applicantCount);
                        }
                ).toList();

        final Page<RunnerPostResponse.SimpleInMyPage> pageResponse
                = new PageImpl<>(responses, pageable, pageRunnerPosts.getTotalElements());

        return ResponseEntity.ok(PageResponse.from(pageResponse));
    }

    private List<Long> collectApplicantCounts(final Page<RunnerPost> pageRunnerPosts) {
        final List<Long> runnerPostIds = pageRunnerPosts.stream()
                .map(RunnerPost::getId)
                .toList();

        return runnerPostQueryService.readCountsByRunnerPostIds(runnerPostIds);
    }
}
