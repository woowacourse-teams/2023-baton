package touch.baton.domain.runnerpost.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.tag.RunnerPostTag;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RunnerPostNativeService {

    private final RunnerPostRepository runnerPostRepository;

    public List<RunnerPost> findNative(final Long cursor, final int limit, final ReviewStatus reviewStatus) {
        final List<RunnerPost> runnerPosts = runnerPostRepository.findByPageInfoAndReviewStatus(cursor, limit, reviewStatus);
        return runnerPosts;
    }

    public List<RunnerPostTag> findRunnerPostTags(final List<RunnerPost> runnerPosts) {
        return runnerPostRepository.findByRunnerPosts(runnerPosts);
    }
}
