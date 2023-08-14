package touch.baton.domain.technicaltag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.technicaltag.RunnerTechnicalTag;

public interface RunnerTechnicalTagRepository extends JpaRepository<RunnerTechnicalTag, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM RunnerTechnicalTag rt WHERE rt.runner = :runner")
    int deleteByRunner(@Param("runner") final Runner runner);
}
