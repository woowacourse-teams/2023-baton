package touch.baton.domain.tag.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.common.vo.TagName;
import touch.baton.domain.tag.command.Tag;
import touch.baton.domain.tag.command.vo.TagReducedName;

import java.util.List;
import java.util.Optional;

public interface TagQueryRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByTagName(final TagName tagName);

    @Query("""
            select t
            from Tag t
            where t.tagReducedName like :tagReducedName%
            order by t.tagReducedName asc
            limit 10
            """)
    List<Tag> readTagsByReducedName(@Param("tagReducedName") final TagReducedName tagReducedName);
}
