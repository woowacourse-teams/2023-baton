package touch.baton.domain.oauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.supporter.Supporter;

import java.util.Optional;

public interface OauthSupporterRepository extends JpaRepository<Supporter, Long> {

    @Query("""
        select s, s.member
        from Supporter s
        join fetch Member m on m.id = s.member.id
        where m.socialId.value = :socialId
        """)
    Optional<Supporter> joinByMemberSocialId(@Param("socialId") final String socialId);
}
