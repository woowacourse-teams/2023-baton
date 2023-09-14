package touch.baton.domain.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.common.vo.TagName;
import touch.baton.domain.tag.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByTagName(final TagName tagName);

    @Query("""
            SELECT t
            FROM Tag t
            WHERE t.tagReducedName.value LIKE :tagReducedName%
            ORDER BY t.tagReducedName.value ASC
            LIMIT 10
            """)
    List<Tag> readTagsByReducedName(@Param("tagReducedName") String tagReducedName);
}
