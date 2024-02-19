package touch.baton.domain.member.query.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.command.vo.ReviewCount;

import java.util.List;

import static touch.baton.domain.member.command.QMember.member;
import static touch.baton.domain.member.command.QSupporter.supporter;

@RequiredArgsConstructor
@Repository
public class RankQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Supporter> findMostReviewSupporterByCount(final int count) {
        return jpaQueryFactory.selectFrom(supporter)
                .join(supporter.member, member).fetchJoin()
                .where(supporter.reviewCount.ne(new ReviewCount(0)))
                .orderBy(supporter.reviewCount.value.desc())
                .limit(count)
                .fetch();
    }
}
