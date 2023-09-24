package touch.baton.domain.runnerpost.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.vo.TagReducedName;

import java.util.List;

import static touch.baton.domain.member.QMember.member;
import static touch.baton.domain.runner.QRunner.runner;
import static touch.baton.domain.runnerpost.QRunnerPost.runnerPost;
import static touch.baton.domain.tag.QRunnerPostTag.runnerPostTag;
import static touch.baton.domain.tag.QTag.tag;

@RequiredArgsConstructor
@Repository
public class RunnerPostCustomRepositoryImpl implements RunnerPostCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RunnerPost> findByPageInfoAndReviewStatus(final Long previousLastId, final int limit, final ReviewStatus reviewStatus) {
        return jpaQueryFactory.selectFrom(runnerPost)
                .join(runnerPost.runner, runner).fetchJoin()
                .join(runner.member, member).fetchJoin()
                .where(runnerPost.id.lt(previousLastId).and(runnerPost.reviewStatus.eq(reviewStatus)))
                .orderBy(runnerPost.id.desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<RunnerPost> findLatestByLimitAndReviewStatus(final int limit, final ReviewStatus reviewStatus) {
        return jpaQueryFactory.selectFrom(runnerPost)
                .join(runnerPost.runner, runner).fetchJoin()
                .join(runner.member, member).fetchJoin()
                .where(runnerPost.reviewStatus.eq(reviewStatus))
                .orderBy(runnerPost.id.desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<RunnerPost> findByPageInfoAndReviewStatusAndTagReducedName(final Long previousLastId,
                                                                           final int limit,
                                                                           final TagReducedName tagReducedName,
                                                                           final ReviewStatus reviewStatus
    ) {
        return jpaQueryFactory.selectFrom(runnerPost)
                .join(runnerPost.runner, runner).fetchJoin()
                .join(runner.member, member).fetchJoin()
                .leftJoin(runnerPost.runnerPostTags.runnerPostTags, runnerPostTag)
                .leftJoin(runnerPostTag.tag, tag)
                .where(tag.tagReducedName.eq(tagReducedName)
                    .and(runnerPost.reviewStatus.eq(reviewStatus))
                    .and(runnerPost.id.lt(previousLastId)))
                .orderBy(runnerPost.id.desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<RunnerPost> findLatestByLimitAndTagNameAndReviewStatus(final int limit,
                                                                       final TagReducedName tagReducedName,
                                                                       final ReviewStatus reviewStatus
    ) {
        return jpaQueryFactory.selectFrom(runnerPost)
                .join(runnerPost.runner, runner).fetchJoin()
                .join(runner.member, member).fetchJoin()
                .leftJoin(runnerPost.runnerPostTags.runnerPostTags, runnerPostTag)
                .leftJoin(runnerPostTag.tag, tag)
                .where(tag.tagReducedName.eq(tagReducedName)
                        .and(runnerPost.reviewStatus.eq(reviewStatus)))
                .orderBy(runnerPost.id.desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<RunnerPostTag> findByRunnerPosts(final List<RunnerPost> runnerPosts) {
        return jpaQueryFactory.selectFrom(runnerPostTag)
                .join(runnerPostTag.tag, tag).fetchJoin()
                .where(runnerPostTag.runnerPost.in(runnerPosts))
                .fetch();
    }
}
