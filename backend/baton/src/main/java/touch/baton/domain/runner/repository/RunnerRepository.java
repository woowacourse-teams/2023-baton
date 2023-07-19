package touch.baton.domain.runner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.runner.Runner;

import java.util.Optional;

public interface RunnerRepository extends JpaRepository<Runner, Long> {

    @Query("""
            select r
            from Runner r
            join fetch Member m on m.id = r.id
            where r.id = :runnerId
            """)
    Optional<Runner> findByIdJoinMember(@Param("runnerId") Long runnerId);
}
