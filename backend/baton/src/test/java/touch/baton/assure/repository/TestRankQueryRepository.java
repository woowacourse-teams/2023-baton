package touch.baton.assure.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.member.query.repository.RankQuerydslRepository;

import static touch.baton.domain.member.command.QSupporter.supporter;

@Profile("test")
@Primary
@Repository
public class TestRankQueryRepository extends RankQuerydslRepository {

    private JPAQueryFactory jpaQueryFactory;

    public TestRankQueryRepository(final JPAQueryFactory jpaQueryFactory) {
        super(jpaQueryFactory);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Transactional
    public void updateReviewCount(final long supporterId, final int reviewCount) {
        jpaQueryFactory.update(supporter)
                .set(supporter.reviewCount.value, supporter.reviewCount.value.add(reviewCount))
                .where(supporter.id.eq(supporterId))
                .execute();
    }
}
