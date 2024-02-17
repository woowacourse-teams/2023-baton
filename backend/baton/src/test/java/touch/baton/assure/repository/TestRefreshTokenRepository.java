package touch.baton.assure.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import touch.baton.domain.oauth.command.token.RefreshToken;

import java.time.Duration;

@Repository
public class TestRefreshTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public TestRefreshTokenRepository(final RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(final RefreshToken refreshToken) {
        redisTemplate.opsForHash().put(String.format("token:refresh:%s", refreshToken.getSocialId()), refreshToken.getToken().getValue(), Duration.ofSeconds(30));
    }

    public void expireRefreshToken(final RefreshToken refreshToken) {
        redisTemplate.expire(String.format("token:refresh:%s", refreshToken.getSocialId()), Duration.ZERO);
    }
}
