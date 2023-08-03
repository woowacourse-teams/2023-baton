package touch.baton.domain.oauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.runner.Runner;

import java.util.Optional;

public interface OauthRunnerRepository extends JpaRepository<Runner, Long> {

    @Query("""
        select r, r.member
        from Runner r
        join fetch Member m on m.id = r.member.id
        where m.email.value = :email
        """)
    Optional<Runner> joinByMemberEmail(@Param("email") final String email);
}
