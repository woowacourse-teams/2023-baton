package touch.baton.tobe.domain.member.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.tobe.domain.member.command.Runner;

import java.util.Optional;

public interface RunnerQueryRepository extends JpaRepository<Runner, Long> {

    @Query("""
            select r
            from Runner r
            join fetch Member m on m.id = r.member.id
            where r.id = :runnerId
            """)
    Optional<Runner> joinMemberByRunnerId(@Param("runnerId") Long runnerId);
}
