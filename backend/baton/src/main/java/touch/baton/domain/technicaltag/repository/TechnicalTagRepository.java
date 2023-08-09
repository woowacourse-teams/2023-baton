package touch.baton.domain.technicaltag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.technicaltag.TechnicalTag;

public interface TechnicalTagRepository extends JpaRepository<TechnicalTag, Long> {
}
