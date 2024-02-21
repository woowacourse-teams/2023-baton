package touch.baton.assure.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.runnerpost.command.repository.RunnerPostCommandRepository;

@Profile("test")
public interface TestRunnerPostCommandRepository extends RunnerPostCommandRepository {

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update RunnerPost rp
            set rp.reviewStatus = 'OVERDUE'
            where rp.id = :id
            """)
    void expireRunnerPost(@Param("id") Long runnerPostId);
}
