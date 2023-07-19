package touch.baton.domain.runnerpost.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.service.RunnerService;
import touch.baton.domain.runnerpost.service.RunnerPostService;
import touch.baton.domain.runnerpost.service.dto.RunnerPostCreateRequest;

import java.net.URI;

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
        final Long saveId = runnerPostService.create(runner, request);

        final URI redirectUri = UriComponentsBuilder.fromPath("/api/v1/posts/runner")
                .path("/{id}")
                .buildAndExpand(saveId)
                .toUri();
        return ResponseEntity.created(redirectUri).build();
    }
}
