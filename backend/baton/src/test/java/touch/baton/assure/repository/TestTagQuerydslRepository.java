package touch.baton.assure.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import touch.baton.domain.tag.query.repository.TagQuerydslRepository;

@Repository
public class TestTagQuerydslRepository extends TagQuerydslRepository {

    public TestTagQuerydslRepository(final JPAQueryFactory queryFactory) {
        super(queryFactory);
    }
}
