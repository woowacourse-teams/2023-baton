package touch.baton.domain.runnerpost.query.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.vo.ReviewStatus;
import touch.baton.domain.tag.command.RunnerPostTag;
import touch.baton.domain.tag.command.vo.TagReducedName;

import java.util.List;

import static touch.baton.domain.member.command.QMember.member;
import static touch.baton.domain.member.command.QRunner.runner;
import static touch.baton.domain.member.command.QSupporter.supporter;
import static touch.baton.domain.member.command.QSupporterRunnerPost.supporterRunnerPost;
import static touch.baton.domain.runnerpost.command.QRunnerPost.runnerPost;
import static touch.baton.domain.tag.command.QRunnerPostTag.runnerPostTag;
import static touch.baton.domain.tag.command.QTag.tag;

@RequiredArgsConstructor
@Repository
public class RunnerPostPageRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<RunnerPost> pageByReviewStatusAndTagReducedName(final Long previousLastId,
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

    public List<RunnerPost> pageBySupporterIdAndReviewStatus(final Long previousLastId,
                                                             final int limit,
                                                             final Long supporterId,
                                                             final ReviewStatus reviewStatus
    ) {
        return jpaQueryFactory.selectFrom(runnerPost)
                .join(runnerPost.runner, runner).fetchJoin()
                .join(runner.member, member).fetchJoin()
                .join(runnerPost.supporter, supporter).fetchJoin()
                .where(previousLastIdLt(previousLastId), supporterIdEq(supporterId), reviewStatusEq(reviewStatus))
                .orderBy(runnerPost.id.desc())
                .limit(limit)
                .fetch();
    }

    public List<RunnerPost> pageBySupporterIdAndReviewStatusNotStarted(final Long previousLastId, final int limit, final Long supporterId) {
        return jpaQueryFactory.select(supporterRunnerPost.runnerPost)
                .from(supporterRunnerPost)
                .join(supporterRunnerPost.runnerPost, runnerPost)
                .join(runnerPost.runner, runner).fetchJoin()
                .join(runner.member, member).fetchJoin()
                .where(previousLastIdLt(previousLastId), supporterIdEqInSupporterRunnerPost(supporterId), reviewStatusEq(ReviewStatus.NOT_STARTED))
                .orderBy(runnerPost.id.desc())
                .limit(limit)
                .fetch();
    }

    public List<RunnerPost> pageByRunnerIdAndReviewStatus(final Long previousLastId,
                                                          final int limit,
                                                          final Long runnerId,
                                                          final ReviewStatus reviewStatus
    ) {
        return jpaQueryFactory.selectFrom(runnerPost)
                .join(runnerPost.runner, runner).fetchJoin()
                .join(runner.member, member).fetchJoin()
                .where(previousLastIdLt(previousLastId), runnerIdEq(runnerId), reviewStatusEq(reviewStatus))
                .orderBy(runnerPost.id.desc())
                .limit(limit)
                .fetch();
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

    private BooleanExpression supporterIdEq(final Long supporterId) {
        return runnerPost.supporter.id.eq(supporterId);
    }

    private BooleanExpression runnerIdEq(final Long runnerId) {
        return runnerPost.runner.id.eq(runnerId);
    }

    private BooleanExpression supporterIdEqInSupporterRunnerPost(final Long supporterId) {
        return supporterRunnerPost.supporter.id.eq(supporterId);
    }

    public List<RunnerPostTag> findRunnerPostTagsByRunnerPosts(final List<RunnerPost> runnerPosts) {
        return jpaQueryFactory.selectFrom(runnerPostTag)
                .join(runnerPostTag.tag, tag).fetchJoin()
                .where(runnerPostTag.runnerPost.in(runnerPosts))
                .fetch();
    }
}
