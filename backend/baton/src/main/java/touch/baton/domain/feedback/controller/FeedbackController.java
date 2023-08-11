package touch.baton.domain.feedback.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import touch.baton.domain.feedback.service.FeedbackService;
import touch.baton.domain.feedback.service.SupporterFeedBackCreateRequest;
import touch.baton.domain.oauth.controller.resolver.AuthRunnerPrincipal;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.service.RunnerService;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/v1/feedback")
@RestController
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final RunnerService runnerService;

    @PostMapping("/supporter")
    public ResponseEntity<Void> createSupporterFeedback(@AuthRunnerPrincipal final Runner runner,
                                                        @Valid @RequestBody final SupporterFeedBackCreateRequest request
    ) {
        final Long savedId = feedbackService.createSupporterFeedback(runner, request);

        final URI redirectUri = UriComponentsBuilder.fromPath("/api/v1/feedback/supporter")
                .path("/{id}")
                .buildAndExpand(savedId)
                .toUri();
        return ResponseEntity.created(redirectUri).build();
    }

}
