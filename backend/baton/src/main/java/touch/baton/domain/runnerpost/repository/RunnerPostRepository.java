package touch.baton.domain.runnerpost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.runnerpost.RunnerPost;

public interface RunnerPostRepository extends JpaRepository<RunnerPost, Long> {
}
