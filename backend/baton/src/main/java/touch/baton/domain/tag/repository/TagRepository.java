package touch.baton.domain.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.tag.vo.TagName;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByTagName(final TagName tagName);
}
