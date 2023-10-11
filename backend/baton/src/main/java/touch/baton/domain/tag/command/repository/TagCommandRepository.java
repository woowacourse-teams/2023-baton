package touch.baton.domain.tag.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.common.vo.TagName;
import touch.baton.domain.tag.command.Tag;

import java.util.Optional;

public interface TagCommandRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByTagName(final TagName tagName);
}
