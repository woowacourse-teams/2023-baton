package touch.baton.assure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.tag.command.Tag;
import touch.baton.domain.tag.command.vo.TagReducedName;

import java.util.List;

public interface TestTagQueryRepository extends JpaRepository<Tag, Long> {

    @Query("""
            select t
            from Tag t
            where t.tagReducedName like :tagReducedName%
            order by t.tagReducedName asc
            limit 10
            """)
    List<Tag> findTagsByReducedName(@Param("tagReducedName") final TagReducedName tagReducedName);
}
