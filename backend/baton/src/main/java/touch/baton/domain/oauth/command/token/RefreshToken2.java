package touch.baton.domain.oauth.command.token;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.oauth.command.token.exception.RefreshTokenDomainException;

@EqualsAndHashCode
@Getter
@RedisHash(value = "token:refresh")
public class RefreshToken2 {

    @Id
    private String socialId;
    private Token2 token;
    private Member member;

    @TimeToLive
    private Long timeout;

    @Builder
    public RefreshToken2(final String socialId, final Token2 token, final Member member, final Long timeout) {
        validateNotNull(socialId, token, member, timeout);
        this.socialId = socialId;
        this.token = token;
        this.member = member;
        this.timeout = timeout;
    }

    private void validateNotNull(final String socialId, final Token2 token, final Member member, final Long timeout) {
        if (socialId == null) {
            throw new RefreshTokenDomainException("RefreshToken 의 socialId 는 null 일 수 없습니다.");
        }
        if (token == null) {
            throw new RefreshTokenDomainException("RefreshToken 의 token 은 null 일 수 없습니다.");
        }
        if (member == null) {
            throw new RefreshTokenDomainException("RefreshToken 의 member 는 null 일 수 없습니다.");
        }
        if (timeout == null) {
            throw new RefreshTokenDomainException("RefreshToken 의 timeout 은 null 일 수 없습니다.");
        }
    }

    public boolean isNotOwner(final Token2 target) {
        return !token.equals(target);
    }
}
