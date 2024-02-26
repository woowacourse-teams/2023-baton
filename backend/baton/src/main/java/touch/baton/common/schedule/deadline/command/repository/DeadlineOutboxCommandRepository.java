package touch.baton.common.schedule.deadline.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.common.schedule.deadline.command.DeadlineOutbox;

public interface DeadlineOutboxCommandRepository extends JpaRepository<DeadlineOutbox, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            delete from DeadlineOutbox do
            where do.runnerPostId = :id
            """)
    void deleteByRunnerPostId(@Param("id") Long runnerPostId);
}
