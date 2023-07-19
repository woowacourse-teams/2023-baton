package touch.baton.domain.runnerpost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.common.vo.Title;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.tag.RunnerPostTags;

import java.util.List;
import java.util.Optional;

public interface RunnerPostRepository extends JpaRepository<RunnerPost, Long> {

    List<RunnerPost> findByRunnerId(Long runnerId);
    List<RunnerPost> findBySupporterId(Long supporterId);
    Optional<RunnerPost> findByTitle(Title title);
}
