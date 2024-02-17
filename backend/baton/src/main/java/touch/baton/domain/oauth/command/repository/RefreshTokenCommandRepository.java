package touch.baton.domain.oauth.command.repository;

import org.springframework.data.repository.CrudRepository;
import touch.baton.domain.oauth.command.token.RefreshToken;

public interface RefreshTokenCommandRepository extends CrudRepository<RefreshToken, String> {
}
