package touch.baton.domain.oauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.member.Member;
import touch.baton.domain.oauth.token.RefreshToken;
import touch.baton.domain.oauth.token.Token;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(final Token token);

    Optional<RefreshToken> findByMember(final Member member);
}
