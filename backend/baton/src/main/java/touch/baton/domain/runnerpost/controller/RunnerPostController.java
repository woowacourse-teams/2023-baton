package touch.baton.domain.runnerpost.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import touch.baton.domain.runnerpost.service.RunnerPostService;
import touch.baton.domain.runnerpost.service.dto.RunnerPostUpdateRequest;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/runner")
@RestController
public class RunnerPostController {

    private final RunnerPostService runnerPostService;

    @PutMapping("/{runnerPostId}")
    public ResponseEntity<Void> update(@PathVariable final Long runnerPostId,
                                       @RequestBody final RunnerPostUpdateRequest request
    ) {
        final Long updatedId = runnerPostService.update(runnerPostId, request);
        final URI redirectUri = UriComponentsBuilder.fromPath("/api/v1/posts/runner")
                .path("/{runnerPostId}")
                .buildAndExpand(updatedId)
                .toUri();
        return ResponseEntity.created(redirectUri).build();
    }
}
