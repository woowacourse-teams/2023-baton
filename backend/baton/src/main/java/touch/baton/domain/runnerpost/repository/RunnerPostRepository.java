package touch.baton.domain.runnerpost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.common.vo.Title;
import touch.baton.domain.runnerpost.RunnerPost;

import java.util.List;
import java.util.Optional;

public interface RunnerPostRepository extends JpaRepository<RunnerPost, Long> {

    @Query(value = """
            select rp
            from RunnerPost rp
            join fetch Runner r on r.id = rp.runner.id
            join fetch Member m on m.id = r.member.id
            where rp.id = :runnerPostId
            """)
    Optional<RunnerPost> joinMemberByRunnerPostId(@Param("runnerPostId") final Long runnerPostId);

    List<RunnerPost> findByRunnerId(Long runnerId);
    List<RunnerPost> readBySupporterId(Long supporterId);
    Optional<RunnerPost> readByTitle(Title title);
}
