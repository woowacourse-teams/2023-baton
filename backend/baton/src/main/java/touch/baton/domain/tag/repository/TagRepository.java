package touch.baton.domain.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.tag.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
