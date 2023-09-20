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
    public List<RunnerPost> findByCursorAndReviewStatus(final Long previousLastId, final Long limit, final ReviewStatus reviewStatus) {
        // 1번 방식 쿼리 4개??? 2개 나가야하는데,....
        final List<RunnerPost> runnerPosts = jpaQueryFactory.selectFrom(runnerPost)
                .join(runnerPost.runner, runner).fetchJoin()
                .join(runner.member, member).fetchJoin()
                .where(runnerPost.id.lt(previousLastId).and(runnerPost.reviewStatus.eq(reviewStatus)))
                .orderBy(runnerPost.id.desc())
                .limit(limit)
                .fetch();

        final List<Long> runnerPostIds = runnerPosts.stream()
                .map(RunnerPost::getId)
                .toList();

        jpaQueryFactory.selectFrom(runnerPostTag)
                .join(runnerPostTag.tag, tag).fetchJoin()
                .where(runnerPostTag.runnerPost.id.in(runnerPostIds))
                .fetch();

        return runnerPosts;

        // 2번 방식 페이지네이션 바꾼거
//        return jpaQueryFactory.selectFrom(runnerPost)
//                .join(runnerPost.runner, runner).fetchJoin()
//                .leftJoin(runnerPost.runnerPostTags.runnerPostTags, runnerPostTag)
//                .where(runnerPost.id.lt(previousLastId).and(runnerPost.reviewStatus.eq(reviewStatus)))
//                .distinct()
//                .limit(limit)
//                .orderBy(runnerPost.id.desc())
//                .fetch();
    }

    @Override
    public List<RunnerPost> findByCursorAndReviewStatusAndTagReducedName(final Long cursor,
                                                                         final Long limit,
                                                                         final ReviewStatus reviewStatus,
                                                                         final TagReducedName tagReducedName
    ) {
        return null;
    }
}
