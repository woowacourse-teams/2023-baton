package touch.baton.domain.runnerpost.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.service.RunnerService;
import touch.baton.domain.runnerpost.controller.response.RunnerPostReadResponses;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.service.RunnerPostService;
import touch.baton.domain.runnerpost.service.dto.RunnerPostCreateRequest;
import touch.baton.domain.runnerpost.service.dto.RunnerPostCreateTestRequest;
import touch.baton.domain.runnerpost.service.dto.RunnerPostUpdateRequest;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/runner")
@RestController
public class RunnerPostController {

    private final RunnerPostService runnerPostService;
    private final RunnerService runnerService;

    @PostMapping
    public ResponseEntity<Void> createRunnerPost(@RequestBody RunnerPostCreateRequest request) {
        // TODO 07/19 로그인 기능 개발시 1L 변경 요망
        Runner runner = runnerService.readRunnerWithMember(1L);

        final Long savedId = runnerPostService.createRunnerPost(runner, request);

        final URI redirectUri = UriComponentsBuilder.fromPath("/api/v1/posts/runner")
                .path("/{id}")
                .buildAndExpand(savedId)
                .toUri();
        return ResponseEntity.created(redirectUri).build();
    }

    @PostMapping("/test")
    public ResponseEntity<Void> createRunnerPostVersionTest(@RequestBody RunnerPostCreateTestRequest request) {
        // TODO 07/19 로그인 기능 개발시 1L 변경 요망
        Runner runner = runnerService.readRunnerWithMember(1L);

        final Long savedId = runnerPostService.createRunnerPostTest(runner, request);

        final URI redirectUri = UriComponentsBuilder.fromPath("/api/v1/posts/runner")
                .path("/{id}")
                .buildAndExpand(savedId)
                .toUri();
        return ResponseEntity.created(redirectUri).build();
    }

    @GetMapping("/{runnerPostId}")
    public ResponseEntity<RunnerPostResponse.Detail> readByRunnerPostId(@PathVariable final Long runnerPostId) {
        final RunnerPostResponse.Detail response
                = RunnerPostResponse.Detail.from(runnerPostService.readByRunnerPostId(runnerPostId));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{runnerPostId}")
    public ResponseEntity<Void> deleteByRunnerPostId(@PathVariable final Long runnerPostId) {
        runnerPostService.deleteByRunnerPostId(runnerPostId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{runnerPostId}")
    public ResponseEntity<Void> update(@PathVariable final Long runnerPostId,
                                       @RequestBody final RunnerPostUpdateRequest request
    ) {
        final Long updatedId = runnerPostService.updateRunnerPost(runnerPostId, request);
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
}
