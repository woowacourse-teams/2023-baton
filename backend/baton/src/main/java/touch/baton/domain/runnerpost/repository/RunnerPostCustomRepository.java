package touch.baton.domain.runnerpost.repository;

import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.vo.TagReducedName;

import java.util.List;

public interface RunnerPostCustomRepository {

    List<RunnerPost> findByPageInfoAndReviewStatus(final Long previousLastId, final int limit, final ReviewStatus reviewStatus);

    List<RunnerPost> findLatestByLimitAndReviewStatus(final int limit, final ReviewStatus reviewStatus);

    List<RunnerPost> findByPageInfoAndReviewStatusAndTagReducedName(final Long cursor,
                                                                    final int limit,
                                                                    final TagReducedName tagReducedName,
                                                                    final ReviewStatus reviewStatus);

    List<RunnerPost> findLatestByLimitAndTagNameAndReviewStatus(final int limit,
                                                                final TagReducedName tagReducedName,
                                                                final ReviewStatus reviewStatus);

    List<RunnerPostTag> findByRunnerPosts(final List<RunnerPost> runnerPosts);
}
