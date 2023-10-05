package touch.baton.domain.runnerpost.query.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.domain.common.request.PageParams;
import touch.baton.domain.common.response.PageResponse;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.oauth.query.controller.resolver.AuthMemberPrincipal;
import touch.baton.domain.oauth.query.controller.resolver.AuthRunnerPrincipal;
import touch.baton.domain.oauth.query.controller.resolver.AuthSupporterPrincipal;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.vo.ReviewStatus;
import touch.baton.domain.runnerpost.query.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.query.controller.response.SupporterRunnerPostResponse;
import touch.baton.domain.runnerpost.query.controller.response.SupporterRunnerPostResponses;
import touch.baton.domain.runnerpost.query.service.RunnerPostQueryService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/runner")
@RestController
public class RunnerPostQueryController {

    private final RunnerPostQueryService runnerPostQueryService;

    @GetMapping
    public ResponseEntity<PageResponse<RunnerPostResponse.Simple>> readRunnerPostsByTagNameAndReviewStatus(
            @Valid @ModelAttribute final PageParams pageParams,
            @RequestParam(required = false) final String tagName,
            @RequestParam(required = false) final ReviewStatus reviewStatus
    ) {
        return ResponseEntity.ok(runnerPostQueryService.pageRunnerPostByTagNameAndReviewStatus(tagName, pageParams, reviewStatus));
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
    public ResponseEntity<PageResponse<RunnerPostResponse.Simple>> readRunnerPostBySupporterIdAndReviewStatusDone(
            @Valid @ModelAttribute final PageParams pageParams,
            @RequestParam final Long supporterId
    ) {
        return ResponseEntity.ok(runnerPostQueryService.pageRunnerPostBySupporterIdAndReviewStatus(pageParams, supporterId, ReviewStatus.DONE));
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
    public ResponseEntity<PageResponse<RunnerPostResponse.Simple>> readRunnerPostByLoginedSupporterAndReviewStatus(
            @AuthSupporterPrincipal final Supporter supporter,
            @Valid @ModelAttribute final PageParams pageParams,
            @RequestParam(required = false) final ReviewStatus reviewStatus
    ) {
        return ResponseEntity.ok(runnerPostQueryService.pageRunnerPostBySupporterIdAndReviewStatus(pageParams, supporter.getId(), reviewStatus));
    }

    @GetMapping("/me/runner") public ResponseEntity<PageResponse<RunnerPostResponse.SimpleByRunner>> readRunnerPostByLoginedRunnerAndReviewStatus(
            @AuthRunnerPrincipal final Runner runner,
            @Valid @ModelAttribute final PageParams pageParams,
            @RequestParam(required = false) final ReviewStatus reviewStatus
    ) {
        return ResponseEntity.ok(runnerPostQueryService.pageRunnerPostByRunnerIdAndReviewStatus(pageParams, runner.getId(), reviewStatus));
    }
}
