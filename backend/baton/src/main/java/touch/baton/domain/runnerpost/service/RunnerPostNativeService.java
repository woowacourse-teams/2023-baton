package touch.baton.domain.runnerpost.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.repository.RunnerPostCustomRepository;
import touch.baton.domain.runnerpost.vo.ReviewStatus;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RunnerPostNativeService {

    private final RunnerPostCustomRepository runnerPostCustomRepository;

    public List<RunnerPost> findNative(final Long cursor, final Long limit, final ReviewStatus reviewStatus) {
        return runnerPostCustomRepository.findByCursorAndReviewStatus(cursor, limit, reviewStatus);
    }
}
