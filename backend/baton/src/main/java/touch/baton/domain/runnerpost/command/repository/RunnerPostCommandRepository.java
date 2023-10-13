package touch.baton.domain.runnerpost.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.runnerpost.command.RunnerPost;

public interface RunnerPostCommandRepository extends JpaRepository<RunnerPost, Long> {
}
