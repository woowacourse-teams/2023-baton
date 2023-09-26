package touch.baton.domain.oauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.tobe.domain.member.command.Member;
import touch.baton.tobe.domain.member.command.vo.OauthId;
import touch.baton.tobe.domain.member.command.vo.SocialId;

import java.util.Optional;

public interface OauthMemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByOauthId(final OauthId oauthId);

    Optional<Member> findBySocialId(final SocialId socialId);
}
