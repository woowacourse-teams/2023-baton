package touch.baton.domain.runnerpost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.common.vo.Title;
import touch.baton.domain.runnerpost.RunnerPost;

import java.util.List;
import java.util.Optional;

public interface RunnerPostRepository extends JpaRepository<RunnerPost, Long> {

    List<RunnerPost> readByRunnerId(Long runnerId);
    List<RunnerPost> readBySupporterId(Long supporterId);
    Optional<RunnerPost> readByTitle(Title title);
}
