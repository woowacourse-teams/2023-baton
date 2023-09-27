package touch.baton.domain.feedback.command.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import touch.baton.domain.feedback.command.service.FeedbackCommandService;
import touch.baton.domain.feedback.command.service.dto.SupporterFeedBackCreateRequest;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.oauth.query.controller.resolver.AuthRunnerPrincipal;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/v1/feedback")
@RestController
public class FeedbackCommandController {

    private final FeedbackCommandService feedbackCommandService;

    @PostMapping("/supporter")
    public ResponseEntity<Void> createSupporterFeedback(@AuthRunnerPrincipal final Runner runner,
                                                        @Valid @RequestBody final SupporterFeedBackCreateRequest request
    ) {
        final Long savedId = feedbackCommandService.createSupporterFeedback(runner, request);

        final URI redirectUri = UriComponentsBuilder.fromPath("/api/v1/feedback/supporter")
                .path("/{id}")
                .buildAndExpand(savedId)
                .toUri();
        return ResponseEntity.created(redirectUri).build();
    }

}
