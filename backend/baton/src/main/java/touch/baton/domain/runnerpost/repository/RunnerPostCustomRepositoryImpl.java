package touch.baton.domain.runnerpost.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
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
                                                                           final ReviewStatus reviewStatus,
                                                                           final TagReducedName tagReducedName
    ) {
        jpaQueryFactory.selectFrom(runnerPostTag)
                .join(runnerPostTag.tag, tag).fetchJoin()
                .join(runnerPostTag.runnerPost, runnerPost).fetchJoin()
                .join(runnerPost.runner, runner).fetchJoin()
                .join(runner.member, member).fetchJoin()
                .where(tag.tagReducedName.value.contains(tagReducedName.getValue()))
                .fetch();
        return jpaQueryFactory.select(runnerPost)
                .from(runnerPostTag)
//                .join(runnerPostTag.tag, tag)
//                .join(runnerPostTag.runnerPost, runnerPost)
//                .join(runnerPost.runner, runner)
//                .join(runner.member, member)
                .where(tag.tagReducedName.eq(tagReducedName))
//                        .and(runnerPost.id.lt(previousLastId))
//                        .and(runnerPost.reviewStatus.eq(reviewStatus)))
                .orderBy(runnerPost.id.desc())
                .limit(limit)
                .fetch();
    }
}
