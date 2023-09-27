package touch.baton.domain.oauth.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.command.vo.SocialId;

import java.util.Optional;

public interface OauthSupporterCommandRepository extends JpaRepository<Supporter, Long> {

    @Query("""
            select s, s.member
            from Supporter s
            join fetch Member m on m.id = s.member.id
            where m.socialId = :socialId
            """)
    Optional<Supporter> joinByMemberSocialId(@Param("socialId") final SocialId socialId);
}
