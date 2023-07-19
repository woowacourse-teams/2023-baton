package touch.baton.domain.runnerpost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import touch.baton.domain.runnerpost.RunnerPost;

import java.util.Optional;

public interface RunnerPostRepository extends JpaRepository<RunnerPost, Long> {

    @Query(value = """
            select rp
            from RunnerPost rp
            join fetch Runner r on r.id = rp.runner.id
            join fetch Member m on m.id = r.member.id
            where rp.id = :runnerPostId
            """)
    Optional<RunnerPost> joinMemberByRunnerPostId(final Long runnerPostId);
}
