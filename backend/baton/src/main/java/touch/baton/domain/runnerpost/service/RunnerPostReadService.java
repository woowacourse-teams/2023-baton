package touch.baton.domain.runnerpost.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.vo.TagReducedName;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RunnerPostReadService {

    private final RunnerPostRepository runnerPostRepository;

    public Page<RunnerPost> readRunnerPostByTagNameAndReviewStatus(final Pageable pageable,
                                                                   final String tagName,
                                                                   final ReviewStatus reviewStatus
    ) {
        final TagReducedName tagReducedName = TagReducedName.from(tagName);

        return runnerPostRepository.findByTagReducedNameAndReviewStatus(pageable, tagReducedName, reviewStatus);
    }

    public List<Long> readApplicantCountsByRunnerPostIds(final List<Long> runnerPostIds) {
        return runnerPostRepository.countApplicantsByRunnerPostIds(runnerPostIds);
    }

    public List<RunnerPost> findNative(final Long cursor, final int limit, final ReviewStatus reviewStatus) {
        final List<RunnerPost> runnerPosts = runnerPostRepository.findByPageInfoAndReviewStatus(cursor, limit, reviewStatus);
        final List<RunnerPostTag> runnerPostTags = runnerPostRepository.findByRunnerPosts(runnerPosts);
        return runnerPosts;
    }
}
