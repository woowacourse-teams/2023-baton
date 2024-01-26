package touch.baton.domain.oauth.command.repository;

import org.springframework.data.repository.CrudRepository;
import touch.baton.domain.member.command.vo.SocialId;
import touch.baton.domain.oauth.command.token.RefreshToken2;

public interface RefreshTokenCommandRepository2 extends CrudRepository<RefreshToken2, SocialId> {
}
