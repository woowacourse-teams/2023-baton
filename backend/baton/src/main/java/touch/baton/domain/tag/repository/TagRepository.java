package touch.baton.domain.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.tag.Tag;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Modifying(clearAutomatically = true)
    @Query("""
        update Tag t
        set t.tagCount.value = t.tagCount.value - 1
        where t.id in :tagIds
        """)
    void decreaseTagCountByTagIds(@Param("tagIds") final List<Long> tagIds);
}
