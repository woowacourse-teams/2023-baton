package touch.baton.domain.runner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.runner.Runner;

public interface RunnerRepository extends JpaRepository<Runner, Long> {
}
