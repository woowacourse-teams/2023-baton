package touch.baton.domain.oauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.oauth.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
