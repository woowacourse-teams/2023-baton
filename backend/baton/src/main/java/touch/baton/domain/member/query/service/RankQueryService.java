package touch.baton.domain.member.query.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.member.query.controller.response.RankResponses;
import touch.baton.domain.member.query.repository.RankQuerydslRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RankQueryService {

    private final RankQuerydslRepository rankQueryDslRepository;

    public RankResponses readMostReviewSupporter() {
        final int maxCount = 5;

        return RankResponses.from(rankQueryDslRepository.findMostReviewSupporterByCount(maxCount));
    }
}
