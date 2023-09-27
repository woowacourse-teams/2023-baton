package touch.baton.tobe.domain.technicaltag.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.tobe.domain.member.command.Runner;
import touch.baton.tobe.domain.technicaltag.command.RunnerTechnicalTag;

public interface RunnerTechnicalTagCommandRepository extends JpaRepository<RunnerTechnicalTag, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM RunnerTechnicalTag rt WHERE rt.runner = :runner")
    int deleteByRunner(@Param("runner") final Runner runner);
}
