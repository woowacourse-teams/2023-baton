package touch.baton.domain.oauth.command.token;

import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.vo.SocialId;
import touch.baton.domain.oauth.command.token.exception.RefreshTokenDomainException;

@Getter
@RedisHash(value = "token:refresh")
public class RefreshToken2 {

    @Id
    private SocialId socialId;
    private Token2 token;
    private Member member;

    @TimeToLive
    @Value("${refresh_token.expire_minutes}")
    private Long timeout;

    @Builder
    public RefreshToken2(final SocialId socialId, final Token2 token, final Member member) {
        validateNotNull(socialId, token, member);
        this.socialId = socialId;
        this.token = token;
        this.member = member;
    }

    private void validateNotNull(final SocialId socialId, final Token2 token, final Member member) {
        if (socialId == null) {
            throw new RefreshTokenDomainException("RefreshToken 의 socialId 는 null 일 수 없습니다.");
        }
        if (token == null) {
            throw new RefreshTokenDomainException("RefreshToken 의 token 은 null 일 수 없습니다.");
        }
        if (member == null) {
            throw new RefreshTokenDomainException("RefreshToken 의 member 는 null 일 수 없습니다.");
        }
    }

    public boolean isNotOwner(final Member target) {
        return !this.member.equals(target);
    }
}
