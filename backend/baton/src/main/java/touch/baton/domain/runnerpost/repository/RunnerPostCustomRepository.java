package touch.baton.domain.runnerpost.repository;

import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.tag.vo.TagReducedName;

import java.util.List;

public interface RunnerPostCustomRepository {

    List<RunnerPost> findByCursorAndReviewStatus(final Long cursor, final Long limit, final ReviewStatus reviewStatus);

    List<RunnerPost> findByCursorAndReviewStatusAndTagReducedName(final Long cursor,
                                                                  final Long limit,
                                                                  final ReviewStatus reviewStatus,
                                                                  final TagReducedName tagReducedName);
}
