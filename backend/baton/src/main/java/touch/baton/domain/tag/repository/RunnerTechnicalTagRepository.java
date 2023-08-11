package touch.baton.domain.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.technicaltag.RunnerTechnicalTag;

public interface RunnerTechnicalTagRepository extends JpaRepository<RunnerTechnicalTag, Long> {
}
