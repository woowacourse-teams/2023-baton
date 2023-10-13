package touch.baton.domain.tag.query.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import touch.baton.domain.tag.command.Tag;
import touch.baton.domain.tag.command.vo.TagReducedName;

import java.util.List;

import static touch.baton.domain.tag.command.QTag.tag;

@RequiredArgsConstructor
@Repository
public class TagQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public List<Tag> findByTagReducedName(final TagReducedName tagReducedName, final int limit) {
        return queryFactory.selectFrom(tag)
                .where(tag.tagReducedName.value.startsWith(tagReducedName.getValue()))
                .orderBy(tag.tagReducedName.value.asc(), tag.tagName.value.desc())
                .limit(limit)
                .fetch();
    }
}
