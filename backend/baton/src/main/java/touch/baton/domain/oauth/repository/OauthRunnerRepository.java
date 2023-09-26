package touch.baton.domain.oauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.tobe.domain.member.command.vo.SocialId;
import touch.baton.tobe.domain.member.command.Runner;

import java.util.Optional;

public interface OauthRunnerRepository extends JpaRepository<Runner, Long> {

    @Query("""
            select r, r.member
            from Runner r
            join fetch Member m on m.id = r.member.id
            where m.socialId = :socialId
            """)
    Optional<Runner> joinByMemberSocialId(@Param("socialId") final SocialId socialId);
}
