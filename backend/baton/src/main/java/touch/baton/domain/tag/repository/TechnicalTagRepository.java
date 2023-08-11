package touch.baton.domain.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.technicaltag.TechnicalTag;

public interface TechnicalTagRepository extends JpaRepository<TechnicalTag, Long> {
}
