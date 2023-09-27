package touch.baton.domain.runnerpost.command.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.oauth.query.controller.resolver.AuthRunnerPrincipal;
import touch.baton.domain.oauth.query.controller.resolver.AuthSupporterPrincipal;
import touch.baton.domain.runnerpost.command.service.RunnerPostCommandService;
import touch.baton.domain.runnerpost.command.service.dto.RunnerPostApplicantCreateRequest;
import touch.baton.domain.runnerpost.command.service.dto.RunnerPostCreateRequest;
import touch.baton.domain.runnerpost.command.service.dto.RunnerPostUpdateRequest;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/runner")
@RestController
public class RunnerPostCommandController {

    private final RunnerPostCommandService runnerPostCommandService;

    @PostMapping
    public ResponseEntity<Void> createRunnerPost(@AuthRunnerPrincipal final Runner runner,
                                                 @Valid @RequestBody final RunnerPostCreateRequest request
    ) {
        final Long savedId = runnerPostCommandService.createRunnerPost(runner, request);
        final URI redirectUri = UriComponentsBuilder.fromPath("/api/v1/posts/runner")
                .path("/{id}")
                .buildAndExpand(savedId)
                .toUri();
        return ResponseEntity.created(redirectUri).build();
    }

    @DeleteMapping("/{runnerPostId}")
    public ResponseEntity<Void> deleteByRunnerPostId(@AuthRunnerPrincipal final Runner runner,
                                                     @PathVariable final Long runnerPostId
    ) {
        runnerPostCommandService.deleteByRunnerPostId(runnerPostId, runner);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{runnerPostId}/application")
    public ResponseEntity<Void> createRunnerPostApplicant(
            @AuthSupporterPrincipal final Supporter supporter,
            @PathVariable final Long runnerPostId,
            @RequestBody @Valid final RunnerPostApplicantCreateRequest request
    ) {
        runnerPostCommandService.createRunnerPostApplicant(supporter, request, runnerPostId);

        final URI redirectUri = UriComponentsBuilder.fromPath("/api/v1/posts/runner")
                .path("/{runnerPostId}")
                .buildAndExpand(runnerPostId)
                .toUri();

        return ResponseEntity.created(redirectUri).build();
    }

    @PatchMapping("/{runnerPostId}/cancelation")
    public ResponseEntity<Void> updateSupporterCancelRunnerPost(@AuthSupporterPrincipal final Supporter supporter,
                                                                @PathVariable final Long runnerPostId
    ) {
        runnerPostCommandService.deleteSupporterRunnerPost(supporter, runnerPostId);
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
        runnerPostCommandService.updateRunnerPostAppliedSupporter(runner, runnerPostId, request);

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
        runnerPostCommandService.updateRunnerPostReviewStatusDone(runnerPostId, supporter);

        final URI redirectUri = UriComponentsBuilder.fromPath("/api/v1/posts/runner")
                .path("/{runnerPostId}")
                .buildAndExpand(runnerPostId)
                .toUri();
        return ResponseEntity.noContent().location(redirectUri).build();
    }
}
