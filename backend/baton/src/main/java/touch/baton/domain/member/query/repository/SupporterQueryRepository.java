package touch.baton.domain.member.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.member.command.Supporter;

import java.util.Optional;

public interface SupporterQueryRepository extends JpaRepository<Supporter, Long> {

    @Query("""
            select s
            from Supporter s
            join fetch Member m on s.member.id = m.id
            where s.id = :supporterId
            """)
    Optional<Supporter> joinMemberBySupporterId(@Param("supporterId") final Long supporterId);
}
