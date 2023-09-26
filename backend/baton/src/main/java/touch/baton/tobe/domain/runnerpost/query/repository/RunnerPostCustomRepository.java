package touch.baton.tobe.domain.runnerpost.query.repository;

import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.vo.TagReducedName;
import touch.baton.tobe.domain.runnerpost.command.RunnerPost;
import touch.baton.tobe.domain.runnerpost.command.vo.ReviewStatus;

import java.util.List;

public interface RunnerPostCustomRepository {

    List<RunnerPost> pageByReviewStatusAndTagReducedName(final Long cursor,
                                                         final int limit,
                                                         final TagReducedName tagReducedName,
                                                         final ReviewStatus reviewStatus);

    List<RunnerPostTag> findRunnerPostTagsByRunnerPosts(final List<RunnerPost> runnerPosts);
}
