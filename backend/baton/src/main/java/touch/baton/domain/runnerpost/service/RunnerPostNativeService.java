package touch.baton.domain.runnerpost.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.vo.ReviewStatus;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RunnerPostNativeService {

//    private final RunnerPostCustomRepositoryImpl runnerPostCustomRepositoryImpl;

    public List<RunnerPost> findNative(final Long cursor, final int limit, final ReviewStatus reviewStatus) {
//        return runnerPostCustomRepositoryImpl.findByReviewStatus(cursor, limit, reviewStatus);
        return null;
    }
}
