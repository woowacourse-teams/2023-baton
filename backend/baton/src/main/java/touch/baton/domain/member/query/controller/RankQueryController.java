package touch.baton.domain.member.query.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.domain.member.query.controller.response.RankResponses;
import touch.baton.domain.member.query.service.RankQueryService;

@RequiredArgsConstructor
@RequestMapping("/api/v1/rank")
@RestController
public class RankQueryController {

    private final RankQueryService rankQueryService;

    @GetMapping("/supporter")
    public ResponseEntity<RankResponses> readMostReviewSupporter() {
        return ResponseEntity.ok(rankQueryService.readMostReviewSupporter());
    }

}
