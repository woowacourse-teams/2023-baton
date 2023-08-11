package touch.baton.domain.supporter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.supporter.SupporterRunnerPost;

import java.util.List;

public interface SupporterRunnerPostRepository extends JpaRepository<SupporterRunnerPost, Long> {

    List<Integer> countByRunnerPostIdIn(@Param("runnerPostIds") List<Long> runnerPostIds);
}
