package touch.baton.domain.oauth.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.oauth.command.token.RefreshToken;
import touch.baton.domain.oauth.command.token.Token;

import java.util.Optional;

public interface RefreshTokenCommandRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(final Token token);

    Optional<RefreshToken> findByMember(final Member member);
}
