package touch.baton.common.schedule.deadline.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import touch.baton.domain.runnerpost.command.RunnerPost;

public interface RunnerPostDeadlineCommandRepository extends JpaRepository<RunnerPost, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update RunnerPost rp
            set rp.reviewStatus = 'OVERDUE'
            where rp.deadline.value <= local_datetime()
            and rp.reviewStatus = 'NOT_STARTED'
            """)
    void updateAllPassedDeadline();
}
