package touch.baton.domain.oauth.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.vo.OauthId;
import touch.baton.domain.member.command.vo.SocialId;

import java.util.Optional;

public interface OauthMemberCommandRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByOauthId(final OauthId oauthId);

    Optional<Member> findBySocialId(final SocialId socialId);
}
