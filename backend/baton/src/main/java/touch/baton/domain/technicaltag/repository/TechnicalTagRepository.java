package touch.baton.domain.technicaltag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.common.vo.TagName;
import touch.baton.domain.technicaltag.TechnicalTag;

import java.util.Optional;

public interface TechnicalTagRepository extends JpaRepository<TechnicalTag, Long> {

    Optional<TechnicalTag> findByTagName(final TagName tagName);
}
