package touch.baton.common.schedule.deadline.query.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.runnerpost.command.RunnerPost;

import java.util.Optional;

public interface RunnerPostDeadlineQueryRepository extends JpaRepository<RunnerPost, Long> {

    @Query("""
            select rp
            from RunnerPost rp
            join fetch rp.supporter
            where rp.id = :id
            """)
    Optional<RunnerPost> joinSupporterByRunnerPostId(@Param("id") Long runnerPostId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select rp
            from RunnerPost rp
            join fetch rp.supporter
            where rp.id = :id
            """)
    Optional<RunnerPost> joinSupporterByRunnerPostIdWithLock(@Param("id") Long runnerPostId);
}
