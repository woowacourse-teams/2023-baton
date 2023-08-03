package touch.baton.domain.oauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.vo.OauthId;

import java.util.Optional;

public interface OauthMemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByOauthId(final OauthId oauthId);
}
