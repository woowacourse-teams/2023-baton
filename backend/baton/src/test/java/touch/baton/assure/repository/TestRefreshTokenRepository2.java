package touch.baton.assure.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import touch.baton.domain.oauth.command.token.RefreshToken2;

import java.time.Duration;

@Repository
public class TestRefreshTokenRepository2  {

    private final RedisTemplate<String, String> redisTemplate;

    public TestRefreshTokenRepository2(final RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void expireRefreshToken(final RefreshToken2 refreshToken) {
        redisTemplate.expire(String.format("token:refresh:%s", refreshToken.getSocialId()), Duration.ZERO);
    }
}
