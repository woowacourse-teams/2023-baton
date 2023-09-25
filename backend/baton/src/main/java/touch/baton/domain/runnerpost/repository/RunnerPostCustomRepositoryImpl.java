package touch.baton.domain.runnerpost.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
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
    public List<RunnerPost> findByPageInfoAndReviewStatusAndTagReducedName(final Long previousLastId,
                                                                           final int limit,
                                                                           final TagReducedName tagReducedName,
                                                                           final ReviewStatus reviewStatus
    ) {
        final JPAQuery<RunnerPost> query = jpaQueryFactory.selectFrom(runnerPost)
                .join(runnerPost.runner, runner).fetchJoin()
                .join(runner.member, member).fetchJoin()
                .where(previousLastIdLt(previousLastId), reviewStatusEq(reviewStatus))
                .orderBy(runnerPost.id.desc())
                .limit(limit);

        if (tagReducedName != null) {
            query.leftJoin(runnerPost.runnerPostTags.runnerPostTags, runnerPostTag)
                    .leftJoin(runnerPostTag.tag, tag)
                    .where(tag.tagReducedName.eq(tagReducedName));
        }

        return query.fetch();
    }

    private BooleanExpression previousLastIdLt(final Long previousLastId) {
        if (previousLastId == null) {
            return null;
        }
        return runnerPost.id.lt(previousLastId);
    }

    private BooleanExpression reviewStatusEq(final ReviewStatus reviewStatus) {
        if (reviewStatus == null) {
            return null;
        }
        return runnerPost.reviewStatus.eq(reviewStatus);
    }

    @Override
    public List<RunnerPostTag> findRunnerPostTagsByRunnerPosts(final List<RunnerPost> runnerPosts) {
        return jpaQueryFactory.selectFrom(runnerPostTag)
                .join(runnerPostTag.tag, tag).fetchJoin()
                .where(runnerPostTag.runnerPost.in(runnerPosts))
                .fetch();
    }
}
