package touch.baton.domain.supporter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.supporter.Supporter;

public interface SupporterRepository extends JpaRepository<Supporter, Long> {
}
