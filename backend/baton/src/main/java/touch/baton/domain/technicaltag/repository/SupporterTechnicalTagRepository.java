package touch.baton.domain.technicaltag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.technicaltag.SupporterTechnicalTag;
import touch.baton.domain.technicaltag.TechnicalTag;

import java.util.Optional;

public interface SupporterTechnicalTagRepository extends JpaRepository<SupporterTechnicalTag, Long> {

    Optional<SupporterTechnicalTag> findBySupporterAndTechnicalTag(final Supporter supporter, final TechnicalTag technicalTag);
}
