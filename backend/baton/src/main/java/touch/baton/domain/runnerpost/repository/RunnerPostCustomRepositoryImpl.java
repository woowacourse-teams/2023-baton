package touch.baton.domain.runnerpost.repository;

import lombok.RequiredArgsConstructor;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.vo.ReviewStatus;

import java.util.List;

@RequiredArgsConstructor
//@Repository
public class RunnerPostCustomRepositoryImpl implements RunnerPostCustomRepository {

//    private final JPAQueryFactory jpaQueryFactory;

//    public List<RunnerPost> findByReviewStatus(final Long cursor, final int limit, final ReviewStatus reviewStatus) {
//        final Query query = em.createNativeQuery("""
//            SELECT rp, rpt, t, r
//            FROM runner_post rp
//            LEFT JOIN runner_post_tag rpt ON rpt.runner_post_id = rp.id
//            LEFT JOIN tag t ON rpt.tag_id = t.id
//            LEFT JOIN runner r ON rp.runner_id = r.id
//            WHERE rp.review_status = :reviewStatus
//            AND rp.id > :cursor
//            ORDER BY rp.id DESC
//            """, RunnerPost.class
//        );
//
//        query.setParameter("cursor", cursor);
//        query.setParameter("reviewStatus", reviewStatus.name());
//        query.setMaxResults(limit);
//
//        return query.getResultList();
//    }

    @Override
    public List<RunnerPost> findByCursorAndReviewStatus(final Long cursor, final int limit, final ReviewStatus reviewStatus) {

//        return jpaQueryFactory.selectFrom(QRunn)
//                .distinct()
//                .fetchJoin();
        return null;
    }
}
