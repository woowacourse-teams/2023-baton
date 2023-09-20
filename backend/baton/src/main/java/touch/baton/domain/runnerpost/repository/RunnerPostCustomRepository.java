package touch.baton.domain.runnerpost.repository;

import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.vo.ReviewStatus;

import java.util.List;

public interface RunnerPostCustomRepository {

    List<RunnerPost> findByCursorAndReviewStatus(final Long cursor, final int limit, final ReviewStatus reviewStatus);
}
